/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/UserMainAuctionDto.java
 * 설명: 사용자 메인 페이지의 경매 카드 하나에 표시될 요약 정보를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class UserMainAuctionDto {
    private Long auctionId;
    private String auctionTitle;
    private String imageUrl;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private OffsetDateTime startTime; // '예정' 상태일 때 시작 시간을 알려주기 위해 추가
    private OffsetDateTime endTime;
    private String status; // 프론트엔드가 '진행중'인지 '예정'인지 구분하기 위해 추가
}