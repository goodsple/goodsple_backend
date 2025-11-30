package com.goodsple.features.admin.auction.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class BidHistoryInfo {
    private OffsetDateTime time;
    private String bidder;
    private BigDecimal price;
}