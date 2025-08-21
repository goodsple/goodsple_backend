/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/UserMainPageResponseDto.java
 * 설명: 사용자 메인 페이지 전체에 필요한 데이터를 담는 메인 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMainPageResponseDto {
    // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 구조를 단순화했습니다. ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    private UserMainAuctionDto mainAuction; // 진행중 또는 다음 예정 경매 1개 (없으면 null)
    // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
}
