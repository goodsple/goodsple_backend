/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/UserAuctionService.java
 * 설명: 사용자용 경매 기능의 비즈니스 로직을 처리합니다.
 */
package com.goodsple.features.auction.service;

import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auction.dto.response.AuctionPageDataResponse;
import com.goodsple.features.auction.dto.response.UserMainAuctionDto;
import com.goodsple.features.auction.dto.response.UserMainPageResponseDto;
import com.goodsple.features.auction.util.AuctionRedisKeyManager; // [추가] Redis 키 매니저 import
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate; // [추가] RedisTemplate import
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal; // [추가] BigDecimal import
import java.util.List;
import java.util.Map; // [추가] Map import

@Service
@RequiredArgsConstructor
public class UserAuctionService {

    private final AuctionMapper auctionMapper;
    private final RedisTemplate<String, Object> redisTemplate; // [추가] RedisTemplate 의존성 주입
    private final AuctionRedisKeyManager keyManager;           // [추가] AuctionRedisKeyManager 의존성 주입
    private static final int UPCOMING_AUCTION_LIMIT = 5;
    private static final int RECENTLY_ENDED_AUCTION_LIMIT = 5;

    @Transactional(readOnly = true)
    public AuctionPageDataResponse getAuctionPageData(Long auctionId) {
        // 1. 현재 로그인한 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 2. [수정] DB에서 경매 페이지의 '기본' 데이터를 먼저 조회합니다.
        AuctionPageDataResponse response = auctionMapper.findAuctionPageDataById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "경매를 찾을 수 없습니다."));

        // 3. [추가] Redis에서 '실시간' 데이터를 조회하여 DB 데이터에 덮어씁니다.
        String stateKey = keyManager.getAuctionStateKey(auctionId);
        Map<Object, Object> redisState = redisTemplate.opsForHash().entries(stateKey);

        // Redis에 실시간 정보가 존재하면 (즉, 경매가 활성화된 상태이면)
        if (!redisState.isEmpty()) {
            AuctionPageDataResponse.AuctionStatus status = response.getStatus();

            // 현재가를 Redis 데이터로 덮어쓰기
            Object currentPriceObj = redisState.get("currentPrice");
            if (currentPriceObj != null) {
                status.setCurrentPrice(new BigDecimal(currentPriceObj.toString()));
            }

            // 최고 입찰자 닉네임을 Redis 데이터로 덮어쓰기
            Object topBidderObj = redisState.get("topBidderNickname");
            if (topBidderObj != null) {
                status.setHighestBidderNickname(topBidderObj.toString());
            }

            // 종료 시간을 Redis 데이터로 덮어쓰기 (시간 연장 대응)
            Object endTimeObj = redisState.get("endTime");
            if (endTimeObj != null) {
                status.setEndTime(endTimeObj.toString());
            }
        }

        // 4. [기존 로직] 응답 데이터에 현재 사용자 정보 추가
        response.getCurrentUser().setNickname(userDetails.getNickname());
        // isBanned 정보는 DB에서 가져온 값을 사용

        return response;
    }

    /**
     * 사용자 메인 페이지에 필요한 대표 경매 정보를 조회하는 메소드
     */
    @Transactional(readOnly = true)
    public UserMainPageResponseDto getMainPageAuction() {
        UserMainAuctionDto mainAuction = auctionMapper.findMainAuction();

        // [추가] 메인 페이지의 '진행중' 경매 카드에도 실시간 현재가를 반영해줍니다.
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