/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/AuctionRealtimeService.java
 * 설명: 라이브-경매의 실시간 상태를 Redis로 관리하고, WebSocket으로 전파하는 서비스입니다.
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionRealtimeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;

    // 경매 시작 시 호출될 메소드 (스케줄러 등에서 호출)
    public void startAuction(AuctionState initialState) {
        String stateKey = keyManager.getAuctionStateKey(initialState.getAuctionId());
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // AuctionState 객체의 필드를 Redis Hash에 한 번에 저장
        hashOps.put(stateKey, "currentPrice", initialState.getCurrentPrice());
        hashOps.put(stateKey, "minBidUnit", initialState.getMinBidUnit());
        hashOps.put(stateKey, "topBidderNickname", "없음");
        hashOps.put(stateKey, "topBidderId", -1L); // 낙찰자 없음
        hashOps.put(stateKey, "endTime", initialState.getEndTime());

        log.info("경매 ID {} 상태 Redis에 초기화 완료.", initialState.getAuctionId());
    }


    public void processBid(Long auctionId, CustomUserDetails userDetails, BidRequest bidRequest) {
        String stateKey = keyManager.getAuctionStateKey(auctionId);
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // TODO: Redis 분산 락(Distributed Lock)을 사용하여 동시 입찰 문제를 처리하는 로직 추가 필요

        // 1. Redis에서 현재 경매 정보 조회
        AuctionState currentState = Objects.requireNonNull(hashOps.entries(stateKey))
                .entrySet().stream()
                .collect(AuctionState::new, (state, entry) -> {
                    switch (entry.getKey()) {
                        case "currentPrice" -> state.setCurrentPrice(new BigDecimal(entry.getValue().toString()));
                        case "minBidUnit" -> state.setMinBidUnit(new BigDecimal(entry.getValue().toString()));
                        case "topBidderNickname" -> state.setTopBidderNickname((String) entry.getValue());
                        case "topBidderId" -> state.setTopBidderId(Long.valueOf(entry.getValue().toString()));
                        case "endTime" -> state.setEndTime(OffsetDateTime.parse(entry.getValue().toString()));
                    }
                }, (state1, state2) -> {});

        if (currentState.getEndTime() == null) {
            log.warn("종료된 경매(ID: {})에 대한 입찰 시도.", auctionId);
            // TODO: 입찰 실패 메시지 전송
            return;
        }

        // 2. 입찰 유효성 검사
        if (bidRequest.getAmount().compareTo(currentState.getCurrentPrice().add(currentState.getMinBidUnit())) < 0) {
            log.warn("유효하지 않은 입찰 금액입니다.");
            // TODO: 입찰 실패 메시지 전송
            return;
        }

        // 3. Redis에 새로운 입찰 정보와 경매 상태 업데이트
        hashOps.put(stateKey, "currentPrice", bidRequest.getAmount());
        hashOps.put(stateKey, "topBidderNickname", userDetails.getNickname());
        hashOps.put(stateKey, "topBidderId", userDetails.getUserId());

        // 4. 시간 연장 로직 처리 (종료 60초 전 입찰 시 60초로 연장)
        OffsetDateTime extendedTime = currentState.getEndTime();
        if (OffsetDateTime.now().isAfter(currentState.getEndTime().minusSeconds(60))) {
            extendedTime = OffsetDateTime.now().plusSeconds(60);
            hashOps.put(stateKey, "endTime", extendedTime);
            log.info("경매 ID {} 시간 연장됨.", auctionId);
        }

        // 5. 입찰 내역을 Redis Sorted Set에 추가 (점수는 타임스탬프)
        String bidsKey = keyManager.getAuctionBidsKey(auctionId);
        BidHistoryInfo newBid = BidHistoryInfo.builder()
                .time(OffsetDateTime.now())
                .bidder(userDetails.getNickname())
                .price(bidRequest.getAmount())
                .build();
        redisTemplate.opsForZSet().add(bidsKey, newBid, System.currentTimeMillis());

        // 6. 모든 참여자에게 상태 업데이트 메시지 브로드캐스트
        AuctionStatusUpdateResponse updateResponse = AuctionStatusUpdateResponse.builder()
                .currentPrice(bidRequest.getAmount())
                .topBidderNickname(userDetails.getNickname())
                .extendedEndTime(extendedTime)
                .newBid(newBid)
                .build();

        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, updateResponse);
        log.info("경매 ID {} 상태 업데이트 전파 완료.", auctionId);
    }

    public void broadcastChatMessage(Long auctionId, CustomUserDetails userDetails, ChatRequest chatRequest) {
        ChatMessageResponse chatResponse = ChatMessageResponse.builder()
                .senderNickname(userDetails.getNickname())
                .message(chatRequest.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, chatResponse);
    }
}