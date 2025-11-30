package com.goodsple.features.auction.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class UserMainAuctionDto {
    private Long auctionId;
    private String auctionTitle;
    private String imageUrl;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
}