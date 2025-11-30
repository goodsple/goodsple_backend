package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMainPageResponseDto {
    private UserMainAuctionDto mainAuction; // 진행중 또는 다음 예정 경매 1개 (없으면 null)
}
