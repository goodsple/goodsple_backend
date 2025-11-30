package com.goodsple.features.order.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Long auctionId;
    private Long userId;
    private BigDecimal orderAmount;
    private String orderStatus;
    private OffsetDateTime orderPaymentDeadline;
}