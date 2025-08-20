/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/AuctionAdminResultResponse.java
 * 설명: 경매 결과 페이지의 전체 정보를 담는 메인 DTO입니다.
 */
package com.goodsple.features.admin.auction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "관리자용 경매 결과 상세 조회 응답 DTO")
public class AuctionAdminResultResponse {
    // 상품 정보
    private String productName;
    private String imageUrl;
    private BigDecimal startPrice;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    // 경매 결과
    private BigDecimal finalPrice;
    private String status;
    private String paymentStatus;
    private WinnerInfo winnerInfo;

    // 배송 정보 (결제가 완료된 경우에만 데이터가 있음)
    private ShippingInfo shippingInfo;

    // 입찰 전체 기록
    private List<BidHistoryInfo> bidHistory;
}