/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/entity/Auction.java
 * 설명: 데이터베이스의 'auctions' 테이블과 매핑되는 도메인 엔티티 클래스입니다.
 */
package com.goodsple.features.admin.auction.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter // MyBatis가 결과를 매핑할 때 Setter를 사용할 수 있습니다.
@NoArgsConstructor
public class Auction {

    private Long auctionId;
    private Long userId; // 경매를 등록한 관리자 ID
    private String auctionTitle;
    private String auctionDescription;
    private BigDecimal auctionStartPrice;
    private BigDecimal auctionMinBidUnit;
    private OffsetDateTime auctionStartTime;
    private OffsetDateTime auctionEndTime;
    private String auctionStatus;

    // 생성자나 빌더를 통해 객체를 생성할 수 있습니다.
    @Builder
    public Auction(Long userId, String auctionTitle, String auctionDescription, BigDecimal auctionStartPrice, BigDecimal auctionMinBidUnit, OffsetDateTime auctionStartTime, OffsetDateTime auctionEndTime, String auctionStatus) {
        this.userId = userId;
        this.auctionTitle = auctionTitle;
        this.auctionDescription = auctionDescription;
        this.auctionStartPrice = auctionStartPrice;
        this.auctionMinBidUnit = auctionMinBidUnit;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndTime = auctionEndTime;
        this.auctionStatus = auctionStatus;
    }
}