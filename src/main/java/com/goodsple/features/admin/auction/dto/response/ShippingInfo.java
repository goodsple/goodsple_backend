/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/ShippingInfo.java
 * 설명: 배송지 정보를 담는 서브 DTO입니다.
 */
package com.goodsple.features.admin.auction.dto.response;

import lombok.Data;

@Data
public class ShippingInfo {
    private String name;
    private String phone;
    private String address;
    private String message;
}