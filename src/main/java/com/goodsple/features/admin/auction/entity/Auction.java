package com.goodsple.features.admin.auction.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Auction {

    private Long auctionId;
    private Long userId;
    private String auctionTitle;
    private String auctionDescription;
    private BigDecimal auctionStartPrice;
    private BigDecimal auctionMinBidUnit;
    private OffsetDateTime auctionStartTime;
    private OffsetDateTime auctionEndTime;
    private String auctionStatus;

    @Builder
    public Auction(Long userId, String auctionTitle, String auctionDescription, BigDecimal auctionStartPrice, BigDecimal auctionMinBidUnit, OffsetDateTime auctionStartTime, OffsetDateTime auctionEndTime, String auctionStatus) {
        this.userId = userId;
        this.auctionTitle = auctionTitle;
        this.auctionDescription = auctionDescription;
        this.auctionStartPrice = auctionStartPrice;
        this.auctionMinBidUnit = auctionMinBidUnit;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndTime = auctionEndTime;
        this.auctionStatus = auctionStatus;
    }
}