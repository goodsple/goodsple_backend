package com.goodsple.features.mybids.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class MyBidsResponse {
    private Long auctionId;
    private String productName;
    private String imageUrl;
    private OffsetDateTime auctionEndTime;
    private BigDecimal finalPrice;
    private String paymentStatus;
    private OffsetDateTime paymentDueDate;
}