/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/request/AuctionCreateRequest.java
 * 설명: 신규 경매 생성을 위한 요청 데이터를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "신규 경매 생성 요청 DTO")
public class AuctionCreateRequest {
    @Schema(description = "상품명", requiredMode = Schema.RequiredMode.REQUIRED, example = "한정판 아이유 LP 바이닐")
    @NotBlank
    public String productName;

    @Schema(description = "상품 상세 설명", requiredMode = Schema.RequiredMode.REQUIRED, example = "미개봉 새 제품입니다.")
    @NotBlank
    public String description;

    @Schema(description = "경매 시작가", requiredMode = Schema.RequiredMode.REQUIRED, example = "50000")
    @NotNull @Min(0)
    public BigDecimal startPrice;

    @Schema(description = "최소 입찰 단위", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @NotNull @Min(100)
    public BigDecimal minBidUnit;

    @Schema(description = "경매 시작 시간 (ISO 8601 형식)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-20T21:00:00")
    @NotNull @Future
    public OffsetDateTime startTime;

    @Schema(description = "경매 종료 시간 (ISO 8601 형식)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-20T22:00:00")
    @NotNull @Future
    public OffsetDateTime endTime;

    @Schema(description = "상품 이미지 URL 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<String> imageUrls;
}