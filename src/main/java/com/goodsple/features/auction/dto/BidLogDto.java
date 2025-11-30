package com.goodsple.features.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidLogDto {
    private Long auctionId;
    private Long userId;
    private BigDecimal bidAmount;
    private OffsetDateTime bidCreatedAt;
}
