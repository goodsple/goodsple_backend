package com.goodsple.features.auction.dto.response;

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
public class BidHistoryInfo {
    private Long bidId;
    private Long userId;
    private String userNickname;
    private BigDecimal price;
    private OffsetDateTime timestamp;
}
