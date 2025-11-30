package com.goodsple.features.payment.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInfoResponse {
    private String orderId;
    private String productName;
    private String imageUrl;
    private BigDecimal amount;
}
