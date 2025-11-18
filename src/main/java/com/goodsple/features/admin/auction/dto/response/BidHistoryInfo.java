/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/BidHistoryInfo.java
 * 설명: 전체 입찰 기록의 각 항목을 담는 서브 DTO입니다.
 */
package com.goodsple.features.admin.auction.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class BidHistoryInfo {
    private OffsetDateTime time;
    private String bidder; // 입찰자 닉네임
    private BigDecimal price;
}