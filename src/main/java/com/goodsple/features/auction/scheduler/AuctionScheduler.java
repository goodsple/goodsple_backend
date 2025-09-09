/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/scheduler/AuctionScheduler.java
 * 설명: 경매의 시작과 종료를 자동으로 처리하는 스케줄러입니다.
 */
package com.goodsple.features.auction.scheduler;

import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.dto.BidLogDto;
import com.goodsple.features.auction.dto.response.BidHistoryInfo;
import com.goodsple.features.auction.service.AuctionRealtimeService;
import com.goodsple.features.auction.util.AuctionRedisKeyManager;
import com.goodsple.features.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionMapper auctionMapper;
    private final AuctionRealtimeService auctionRealtimeService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;
    private final SimpMessagingTemplate messagingTemplate; // [추가] SimpMessagingTemplate 의존성 주입

    /**
     * 1분마다 실행되어 시작 시간이 된 경매를 'active' 상태로 변경하고 Redis에 등록합니다.
     */
    @Scheduled(cron = "0 * * * * *") // 매 분 0초에 실행
    @Transactional
    public void startAuctions() {
        log.info("경매 자동 시작 스케줄러 실행...");
        List<AuctionState> auctionsToStart = auctionMapper.findAuctionsToStart(OffsetDateTime.now());

        for (AuctionState auction : auctionsToStart) {
            log.info("경매 ID {}를 시작합니다.", auction.getAuctionId());
            // 1. DB 상태를 'active'로 변경
            auctionMapper.updateAuctionStatus(auction.getAuctionId(), "active");
            // 2. Redis에 실시간 데이터 초기화
            auctionRealtimeService.startAuction(auction);
        }
    }

    /**
     * 1분마다 실행되어 종료 시간이 된 경매를 'ended' 상태로 변경하고,
     * Redis의 최종 결과를 DB에 저장합니다.
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void endAuctions() {
        log.info("경매 자동 종료 스케줄러 실행...");
        List<Long> auctionIdsToEnd = auctionMapper.findAuctionsToEnd(OffsetDateTime.now());

        for (Long auctionId : auctionIdsToEnd) {
            log.info("경매 ID {}를 종료합니다.", auctionId);

            // [추가] DB 업데이트 전에 먼저 경매 종료 알림을 보냅니다.
            // 이렇게 하면 사용자는 종료 사실을 즉시 인지하고, 백엔드는 백그라운드에서 안전하게 데이터 처리를 계속할 수 있습니다.
            String finalWinnerNickname = getFinalWinnerNicknameFromRedis(auctionId); // 아래 추가된 헬퍼 메소드 사용

            Map<String, Object> payload = Map.of(
                    "type", "AUCTION_ENDED",
                    "message", "경매가 종료되었습니다. 최종 낙찰자는 '" + finalWinnerNickname + "'님 입니다.",
                    "winner", finalWinnerNickname
            );
            messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, payload);
            log.info("경매 ID {} 종료 알림 전송 완료.", auctionId);

            // --- 이하 기존 데이터 처리 로직 ---
            String stateKey = keyManager.getAuctionStateKey(auctionId);
            Map<Object, Object> finalState = redisTemplate.opsForHash().entries(stateKey);

            if (finalState.isEmpty()) {
                log.warn("경매 ID {}의 Redis 데이터가 없어 DB 업데이트를 건너뜁니다.", auctionId);
                auctionMapper.updateAuctionStatus(auctionId, "ended");
                continue;
            }

            Long winnerId = Long.valueOf(finalState.get("topBidderId").toString());
            Object finalPriceObj = finalState.get("currentPrice");

            if (winnerId > 0) {
                // 1. 최종 낙찰 정보 DB 업데이트
                BigDecimal finalPrice = new BigDecimal(finalPriceObj.toString());
                auctionMapper.updateAuctionWinner(auctionId, winnerId, finalPrice);

                // 2. Redis 입찰 기록 DB로 이전
                String bidsKey = keyManager.getAuctionBidsKey(auctionId);
                Set<Object> bidObjects = redisTemplate.opsForZSet().range(bidsKey, 0, -1);
                if (bidObjects != null && !bidObjects.isEmpty()) {
                    List<BidLogDto> bidsToSave = bidObjects.stream()
                            .map(obj -> {
                                BidHistoryInfo info = (BidHistoryInfo) obj;
                                // 이제 info 객체에 userId가 있으므로, DB 조회 없이 바로 사용합니다.
                                return new BidLogDto(auctionId, info.getUserId(), info.getBidAmount(), info.getTimestamp());
                            })
                            .collect(Collectors.toList());

                    if (!bidsToSave.isEmpty()) {
                        auctionMapper.insertBidsBatch(bidsToSave);
                    }
                }

                // 3. orders 테이블에 주문 생성
                Order newOrder = Order.builder()
                        .auctionId(auctionId)
                        .userId(winnerId)
                        .orderAmount(finalPrice) // orderFinalPrice -> orderAmount
                        .orderStatus("pending") // 초기 상태는 '결제 대기'
                        .build();
                auctionMapper.insertOrder(newOrder);
            }

            // 4. DB 상태를 'ended'로 변경
            auctionMapper.updateAuctionStatus(auctionId, "ended");

            // 5. Redis 데이터 정리
            redisTemplate.delete(stateKey);
            redisTemplate.delete(keyManager.getAuctionBidsKey(auctionId));
            log.info("경매 ID {}의 Redis 데이터가 정리되었습니다.", auctionId);
        }
    }

    /**
     * [추가] Redis에서 최종 낙찰자의 닉네임을 조회하는 헬퍼(helper) 메소드
     */
    private String getFinalWinnerNicknameFromRedis(Long auctionId) {
        String stateKey = keyManager.getAuctionStateKey(auctionId);
        Object winnerNicknameObj = redisTemplate.opsForHash().get(stateKey, "topBidderNickname");

        if (winnerNicknameObj != null && !winnerNicknameObj.toString().equals("없음")) {
            return winnerNicknameObj.toString();
        } else {
            return "없음"; // 낙찰자가 없는 경우
        }
    }
}