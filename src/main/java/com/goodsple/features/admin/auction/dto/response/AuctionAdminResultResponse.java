package com.goodsple.features.admin.auction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "관리자용 경매 결과 상세 조회 응답 DTO")
public class AuctionAdminResultResponse {
    private String productName;
    private String imageUrl;
    private BigDecimal startPrice;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    private BigDecimal finalPrice;
    private String status;
    private String paymentStatus;
    private WinnerInfo winnerInfo;

    private ShippingInfo shippingInfo;

    private List<BidHistoryInfo> bidHistory;
}