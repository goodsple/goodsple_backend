package com.goodsple.features.auction.service;

import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.dto.request.BidRequest;
import com.goodsple.features.auction.dto.request.ChatRequest;
import com.goodsple.features.auction.dto.response.AuctionStatusUpdateResponse;
import com.goodsple.features.auction.dto.response.BidHistoryInfo;
import com.goodsple.features.auction.dto.response.ChatMessageResponse;
import com.goodsple.features.auction.util.AuctionRedisKeyManager;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auth.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionRealtimeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;
    private final AuctionMapper auctionMapper;
    // private final ProhibitedWordService prohibitedWordService; // TODO: 금지어 기능 연동 시 주석 해제

    public void startAuction(AuctionState initialState) {
        String stateKey = keyManager.getAuctionStateKey(initialState.getAuctionId());
        Map<String, Object> stateMap = Map.of(
                "currentPrice", initialState.getCurrentPrice(),
                "minBidUnit", initialState.getMinBidUnit(),
                "topBidderNickname", "없음",
                "topBidderId", -1L,
                "endTime", initialState.getEndTime().toString()
        );
        redisTemplate.opsForHash().putAll(stateKey, stateMap);
        log.info("경매 ID {} 상태 Redis에 초기화 완료.", initialState.getAuctionId());
    }

    public void processBid(Long auctionId, CustomUserDetails userDetails, BidRequest bidRequest) {

        boolean isBanned = checkAndReleaseAuctionBan(userDetails.getUserId());
        if (isBanned) {
            log.warn("경매 참여가 제한된 사용자(ID: {})의 입찰 시도.", userDetails.getUserId());

            Map<String, String> payload = Map.of(
                    "type", "AUCTION_BAN_ERROR",
                    "message", "경매 참여가 제한된 상태입니다. 입찰할 수 없습니다."
            );

            messagingTemplate.convertAndSendToUser(userDetails.getUsername(), "/queue/errors", payload);

            return;
        }

        String stateKey = keyManager.getAuctionStateKey(auctionId);
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

        // TODO: Redis 분산 락(Distributed Lock)을 사용하여 동시 입찰 문제를 처리하는 로직 추가 필요

        Map<Object, Object> entries = hashOps.entries(stateKey);
        if (entries.isEmpty()) {
            log.warn("진행중인 경매(ID: {}) 정보를 Redis에서 찾을 수 없습니다.", auctionId);
            return;
        }

        AuctionState currentState = new AuctionState(
                auctionId,
                new BigDecimal(entries.get("currentPrice").toString()),
                new BigDecimal(entries.get("minBidUnit").toString()),
                (String) entries.get("topBidderNickname"),
                Long.valueOf(entries.get("topBidderId").toString()),
                OffsetDateTime.parse((String) Objects.requireNonNull(entries.get("endTime")))
        );

        if (OffsetDateTime.now().isAfter(currentState.getEndTime())) {
            log.warn("종료된 경매(ID: {})에 대한 입찰 시도.", auctionId);
            return;
        }

        if (bidRequest.getAmount().compareTo(currentState.getCurrentPrice().add(currentState.getMinBidUnit())) < 0) {
            log.warn("유효하지 않은 입찰 금액입니다.");
            return;
        }

        hashOps.put(stateKey, "currentPrice", bidRequest.getAmount());
        hashOps.put(stateKey, "topBidderNickname", userDetails.getNickname());
        hashOps.put(stateKey, "topBidderId", userDetails.getUserId());

        OffsetDateTime extendedTime = currentState.getEndTime();
        if (OffsetDateTime.now().isAfter(currentState.getEndTime().minusSeconds(60))) {
            extendedTime = OffsetDateTime.now().plusSeconds(60);
            hashOps.put(stateKey, "endTime", extendedTime.toString());
            log.info("경매 ID {} 시간 연장됨.", auctionId);
        }

        String bidsKey = keyManager.getAuctionBidsKey(auctionId);
        BidHistoryInfo newBid = BidHistoryInfo.builder()
                .bidId(System.currentTimeMillis())
                .userId(userDetails.getUserId())
                .userNickname(userDetails.getNickname())
                .price(bidRequest.getAmount())
                .timestamp(OffsetDateTime.now())
                .build();
        redisTemplate.opsForZSet().add(bidsKey, newBid, System.currentTimeMillis());

        AuctionStatusUpdateResponse updateResponse = AuctionStatusUpdateResponse.builder()
                .type("AUCTION_UPDATE")
                .currentPrice(bidRequest.getAmount())
                .topBidderNickname(userDetails.getNickname())
                .extendedEndTime(extendedTime)
                .newBid(newBid)
                .build();

        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, updateResponse);
        log.info("경매 ID {} 상태 업데이트 전파 완료.", auctionId);
    }

    public void broadcastChatMessage(Long auctionId, CustomUserDetails userDetails, ChatRequest chatRequest) {
        log.info("경매 ID {}에 대한 채팅 수신: 사용자 {}, 메시지: {}",
                auctionId, userDetails.getNickname(), chatRequest.getMessage());

        ChatMessageResponse chatResponse = ChatMessageResponse.builder()
                .type("CHAT_MESSAGE")
                .senderNickname(userDetails.getNickname())
                .message(chatRequest.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, chatResponse);
    }

    /**
     * 사용자의 경매 참여 제한 상태를 확인하고, 만료되었다면 해제하는 공통 메소드
     * @param userId 확인할 사용자 ID
     * @return 현재 참여 제한 상태이면 true, 아니면 false
     */
    @Transactional
    public boolean checkAndReleaseAuctionBan(Long userId) {
        User user = auctionMapper.findUserForAuctionBanCheck(userId);

        if (user == null || !user.getIsBannedFromAuction()) {
            return false;
        }

        if (user.getAuctionBanUntil() != null && user.getAuctionBanUntil().isBefore(OffsetDateTime.now())) {
            auctionMapper.releaseAuctionBan(userId);
            log.info("사용자 ID {}의 경매 참여 제한이 만료되어 자동으로 해제되었습니다.", userId);
            return false;
        }

        return true;
    }

    public void removeAuctionFromRedis(Long auctionId) {
        String stateKey = keyManager.getAuctionStateKey(auctionId);
        String bidsKey = keyManager.getAuctionBidsKey(auctionId);
        redisTemplate.delete(List.of(stateKey, bidsKey));
    }
}