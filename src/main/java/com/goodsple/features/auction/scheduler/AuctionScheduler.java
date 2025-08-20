/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/scheduler/AuctionScheduler.java
 * 설명: 경매의 시작과 종료를 자동으로 처리하는 스케줄러입니다.
 */
package com.goodsple.features.auction.scheduler;

import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.dto.response.BidHistoryInfo;
import com.goodsple.features.auction.service.AuctionRealtimeService;
import com.goodsple.features.auction.util.AuctionRedisKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionMapper auctionMapper;
    private final AuctionRealtimeService auctionRealtimeService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;

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
    @Scheduled(cron = "0 * * * * *") // 매 분 0초에 실행
    @Transactional
    public void endAuctions() {
        log.info("경매 자동 종료 스케줄러 실행...");
        List<Long> auctionIdsToEnd = auctionMapper.findAuctionsToEnd(OffsetDateTime.now());

        for (Long auctionId : auctionIdsToEnd) {
            log.info("경매 ID {}를 종료합니다.", auctionId);
            String stateKey = keyManager.getAuctionStateKey(auctionId);
            Map<Object, Object> finalState = redisTemplate.opsForHash().entries(stateKey);

            if (finalState.isEmpty()) {
                log.warn("경매 ID {}의 Redis 데이터가 없어 DB 업데이트를 건너뜁니다.", auctionId);
                auctionMapper.updateAuctionStatus(auctionId, "ended");
                continue;
            }

            // 1. Redis에서 최종 낙찰 정보 가져오기
            Long winnerId = Long.valueOf(finalState.get("topBidderId").toString());
            Object finalPriceObj = finalState.get("currentPrice");

            // 2. 최종 낙찰 정보가 있으면 DB에 업데이트
            if (winnerId > 0) {
                auctionMapper.updateAuctionWinner(auctionId, winnerId, new java.math.BigDecimal(finalPriceObj.toString()));

                // 3. Redis에 저장된 모든 입찰 기록을 DB로 옮기기
                String bidsKey = keyManager.getAuctionBidsKey(auctionId);
                Set<Object> bidObjects = redisTemplate.opsForZSet().range(bidsKey, 0, -1);
                if (bidObjects != null && !bidObjects.isEmpty()) {
                    List<BidHistoryInfo> bidsToSave = bidObjects.stream()
                            .map(obj -> (BidHistoryInfo) obj)
                            .toList();
                    // TODO: bidsToSave를 DB에 저장하는 로직 필요 (Batch Insert)
                }

                // 4. orders 테이블에 주문 생성
                // TODO: orders 테이블에 주문을 생성하는 로직 필요
            }

            // 5. DB 상태를 'ended'로 변경
            auctionMapper.updateAuctionStatus(auctionId, "ended");

            // 6. Redis 데이터 정리
            redisTemplate.delete(stateKey);
            redisTemplate.delete(keyManager.getAuctionBidsKey(auctionId));
            log.info("경매 ID {}의 Redis 데이터가 정리되었습니다.", auctionId);
        }
    }
}