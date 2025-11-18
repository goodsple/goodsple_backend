/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/BidHistoryInfo.java
 * 설명: 입찰 내역 한 건의 정보를 담는 DTO입니다. (프론트엔드 Bid 타입과 일치)
 */
package com.goodsple.features.auction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidHistoryInfo {
    // 프론트엔드의 Bid 타입과 필드 이름을 일치시킵니다.
    private Long bidId;
    private Long userId;
    private String userNickname;
    private BigDecimal price;
    private OffsetDateTime timestamp;
}
