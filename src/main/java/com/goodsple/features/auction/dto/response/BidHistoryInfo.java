/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/BidHistoryInfo.java
 * 설명: 입찰 내역 한 건의 정보를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class BidHistoryInfo {
    private OffsetDateTime time;
    private String bidder; // 입찰자 닉네임
    private BigDecimal price;
}