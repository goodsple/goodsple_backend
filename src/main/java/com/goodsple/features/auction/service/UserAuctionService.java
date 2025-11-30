package com.goodsple.features.auction.service;

import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.dto.response.AuctionPageDataResponse;
import com.goodsple.features.auction.dto.response.UserMainAuctionDto;
import com.goodsple.features.auction.dto.response.UserMainPageResponseDto;
import com.goodsple.features.auction.util.AuctionRedisKeyManager;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuctionService {

    private static final Logger log = LoggerFactory.getLogger(UserAuctionService.class);

    private final AuctionMapper auctionMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRedisKeyManager keyManager;
    private static final int UPCOMING_AUCTION_LIMIT = 5;
    private static final int RECENTLY_ENDED_AUCTION_LIMIT = 5;
    private final AuctionRealtimeService auctionRealtimeService;

    @Transactional
    public AuctionPageDataResponse getAuctionPageData(Long auctionId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        AuctionPageDataResponse response = auctionMapper.findAuctionPageDataById(auctionId, userDetails.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "경매를 찾을 수 없습니다."));

        String stateKey = keyManager.getAuctionStateKey(auctionId);
        Map<Object, Object> redisState = redisTemplate.opsForHash().entries(stateKey);

        if (redisState.isEmpty()) {
            String dbStatus = response.getStatus().getAuctionStatus();
            OffsetDateTime dbStartTime = response.getStatus().getStartTime();

            log.info("========== 디버깅 로그 ==========");
            log.info("DB에서 조회된 상태 (dbStatus): {}", dbStatus);
            log.info("DB에서 조회된 시작 시간 (dbStartTime): {}", dbStartTime);
            log.info("현재 서버 시간 (OffsetDateTime.now()): {}", OffsetDateTime.now());
            if (dbStartTime != null) {
                log.info("시간 비교 결과 (isBefore): {}", dbStartTime.isBefore(OffsetDateTime.now()));
            }
            log.info("==============================");

            if ("scheduled".equalsIgnoreCase(dbStatus) && dbStartTime.isBefore(OffsetDateTime.now())) {
                log.info("사용자 요청에 의해 경매 ID {}를 시작합니다.", auctionId);
                auctionMapper.updateAuctionStatus(auctionId, "active");
                AuctionState initialState = auctionMapper.findInitialAuctionStateById(auctionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "경매 정보를 찾을 수 없습니다."));
                auctionRealtimeService.startAuction(initialState);

                response.getStatus().setAuctionStatus("active");
                response.getStatus().setCurrentPrice(initialState.getCurrentPrice());
                response.getStatus().setHighestBidderNickname("없음");
                response.getStatus().setEndTime(initialState.getEndTime().toString());
            } else {
                log.warn("아직 시작되지 않았거나 이미 종료된 경매(ID: {})입니다.", auctionId);
            }
        }

        if (!redisState.isEmpty()) {
            AuctionPageDataResponse.AuctionStatus status = response.getStatus();
            status.setCurrentPrice(new BigDecimal(redisState.get("currentPrice").toString()));
            status.setHighestBidderNickname((String) redisState.get("topBidderNickname"));
            status.setEndTime(redisState.get("endTime").toString());
        }

        response.getCurrentUser().setNickname(userDetails.getNickname());
        boolean isBanned = auctionRealtimeService.checkAndReleaseAuctionBan(userDetails.getUserId());
        response.getCurrentUser().setBanned(isBanned);

        return response;
    }

    /**
     * 사용자 메인 페이지에 필요한 대표 경매 정보를 조회하는 메소드
     */
    @Transactional(readOnly = true)
    public UserMainPageResponseDto getMainPageAuction() {
        UserMainAuctionDto mainAuction = auctionMapper.findMainAuction();

        if (mainAuction != null && "active".equalsIgnoreCase(mainAuction.getStatus())) {
            String stateKey = keyManager.getAuctionStateKey(mainAuction.getAuctionId());
            Object currentPriceObj = redisTemplate.opsForHash().get(stateKey, "currentPrice");
            if (currentPriceObj != null) {
                mainAuction.setCurrentPrice(new BigDecimal(currentPriceObj.toString()));
            }
        }

        return UserMainPageResponseDto.builder()
                .mainAuction(mainAuction)
                .build();
    }
}