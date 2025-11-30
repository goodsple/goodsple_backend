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