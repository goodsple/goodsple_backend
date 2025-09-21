/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/request/BidRequest.java
 * 설명: 사용자가 입찰 시 프론트엔드에서 보내는 데이터를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BidRequest {
    private BigDecimal amount; // 입찰 금액
}
