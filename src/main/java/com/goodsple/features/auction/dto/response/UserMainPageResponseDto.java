/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/UserMainPageResponseDto.java
 * 설명: 사용자 메인 페이지 전체에 필요한 데이터를 담는 메인 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserMainPageResponseDto {
    // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 이 부분을 수정했습니다. ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    private UserMainAuctionDto mainAuction; // 진행중 또는 예정인 대표 경매 1개
    private List<UserMainAuctionDto> upcomingAuctions; // 그 외 예정 경매 목록 (선택 사항)
    private List<UserMainAuctionDto> recentlyEndedAuctions; // 최근 종료 경매 목록
    // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
}
