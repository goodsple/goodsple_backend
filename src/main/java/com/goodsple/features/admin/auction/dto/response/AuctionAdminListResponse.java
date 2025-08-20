/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/AuctionAdminListResponse.java
 * 설명: 관리자용 경매 목록의 각 항목을 나타내는 DTO입니다.
 */
package com.goodsple.features.admin.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "관리자용 경매 목록 조회 응답 DTO")
public class AuctionAdminListResponse {
    @Schema(description = "경매 ID", example = "1")
    public Long id;

    @Schema(description = "상품명", example = "세븐틴 FML 개봉앨범")
    public String productName;

    @Schema(description = "경매 시작 시간", example = "2025-08-20 21:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public OffsetDateTime startTime;

    @Schema(description = "경매 종료 시간", example = "2025-08-20 22:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public OffsetDateTime endTime;

    @Schema(description = "현재가/낙찰가", example = "12000")
    public BigDecimal currentPrice;

    @Schema(description = "경매 상태", example = "active")
    public String status;

    @Schema(description = "결제 상태", example = "paid")
    public String paymentStatus;
}