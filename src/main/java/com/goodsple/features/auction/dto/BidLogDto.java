/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/BidLogDto.java
 * 설명: Redis의 입찰 기록을 DB에 저장하기 위한 DTO입니다.
 */
package com.goodsple.features.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidLogDto {
    private Long auctionId;
    private Long userId;
    private BigDecimal bidAmount;
    private OffsetDateTime bidCreatedAt;
}
