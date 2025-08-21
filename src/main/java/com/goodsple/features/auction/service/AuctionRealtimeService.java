/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/AuctionRealtimeService.java
 * 설명: 라이브 경매의 실시간 상태를 Redis로 관리하고, WebSocket으로 전파하는 서비스입니다.
 */
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
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionRealtimeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;
    // private final ProhibitedWordService prohibitedWordService; // TODO: 금지어 기능 연동 시 주석 해제

    // ... (startAuction 메소드는 기존과 동일) ...
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

    // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ processBid 메소드 전체를 아래 내용으로 교체합니다. ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    public void processBid(Long auctionId, CustomUserDetails userDetails, BidRequest bidRequest) {
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

        // 5. 입찰 내역을 Redis Sorted Set에 추가
        String bidsKey = keyManager.getAuctionBidsKey(auctionId);
        // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ newBid 객체를 생성할 때 userId를 추가합니다. ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
        BidHistoryInfo newBid = BidHistoryInfo.builder()
                .bidId(System.currentTimeMillis()) // 임시 ID
                .userId(userDetails.getUserId()) // userId 추가!
                .userNickname(userDetails.getNickname())
                .bidAmount(bidRequest.getAmount())
                .timestamp(OffsetDateTime.now())
                .build();
        redisTemplate.opsForZSet().add(bidsKey, newBid, System.currentTimeMillis());

        // 6. 모든 참여자에게 상태 업데이트 메시지 브로드캐스트 (이하 동일)
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
    // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

    // ... (broadcastChatMessage 메소드는 기존과 동일) ...
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
}