package com.goodsple.features.payment.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private String tossOrderId;
    private BigDecimal amount;
    private ShippingInfoRequest shippingInfo;
}
