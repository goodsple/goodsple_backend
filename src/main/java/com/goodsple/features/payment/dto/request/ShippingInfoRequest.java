package com.goodsple.features.payment.dto.request;

import lombok.Data;

@Data
public class ShippingInfoRequest {
    private String name;
    private String phone;
    private String postalCode;
    private String address;
    private String message;
}
