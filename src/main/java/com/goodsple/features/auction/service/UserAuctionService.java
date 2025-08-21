/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/UserAuctionService.java
 * 설명: 사용자용 경매 기능의 비즈니스 로직을 처리합니다.
 */
package com.goodsple.features.auction.service;

import com.goodsple.features.auction.dto.response.AuctionPageDataResponse;
import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.features.auction.dto.response.UserMainAuctionDto;
import com.goodsple.features.auction.dto.response.UserMainPageResponseDto;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuctionService {

    private final AuctionMapper auctionMapper;
    private static final int UPCOMING_AUCTION_LIMIT = 5;
    private static final int RECENTLY_ENDED_AUCTION_LIMIT = 5;

    @Transactional(readOnly = true)
    public AuctionPageDataResponse getAuctionPageData(Long auctionId) {
        // 1. 현재 로그인한 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 2. DB에서 경매 페이지에 필요한 모든 데이터를 조회
        AuctionPageDataResponse response = auctionMapper.findAuctionPageDataById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "진행중인 경매를 찾을 수 없습니다."));

        // 3. 응답 데이터에 현재 사용자 정보 추가
        response.getCurrentUser().setNickname(userDetails.getNickname());
        // isBanned 정보는 DB에서 가져온 값을 사용

        return response;
    }

    /**
     * 사용자 메인 페이지에 필요한 모든 경매 목록을 조회하는 새로운 메소드
     */
    @Transactional(readOnly = true)
    public UserMainPageResponseDto getMainPageAuctions() {
        // 1. 대표 경매(진행중 또는 예정) 1개 조회
        UserMainAuctionDto mainAuction = auctionMapper.findMainAuction();

        // 2. 대표 경매를 제외한 예정 경매 목록 조회
        Long excludeId = (mainAuction != null) ? mainAuction.getAuctionId() : -1L;
        List<UserMainAuctionDto> upcomingAuctions = auctionMapper.findUpcomingAuctions(excludeId, UPCOMING_AUCTION_LIMIT);

        // 3. 최근 종료된 경매 목록 조회
        List<UserMainAuctionDto> recentlyEndedAuctions = auctionMapper.findRecentlyEndedAuctions(RECENTLY_ENDED_AUCTION_LIMIT);

        // 4. 최종 DTO로 조립하여 반환
        return UserMainPageResponseDto.builder()
                .mainAuction(mainAuction)
                .upcomingAuctions(upcomingAuctions)
                .recentlyEndedAuctions(recentlyEndedAuctions)
                .build();
    }
}