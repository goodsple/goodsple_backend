/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/AuctionState.java
 * 설명: Redis에 저장될 라이브 경매의 실시간 상태 정보를 나타내는 DTO입니다.
 */
package com.goodsple.features.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionState {
    private Long auctionId;
    private BigDecimal currentPrice;
    private BigDecimal minBidUnit;
    private String topBidderNickname;
    private Long topBidderId;
    private OffsetDateTime endTime;
}