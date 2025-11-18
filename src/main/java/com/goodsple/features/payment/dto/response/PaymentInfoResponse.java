package com.goodsple.features.payment.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInfoResponse {
    private String orderId; // PG사에서 사용할 주문 ID (예: "ORD_20250911_1")
    private String productName;
    private String imageUrl;
    private BigDecimal amount; // 최종 결제 금액
}
