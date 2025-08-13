/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/request/AuctionStatusUpdateRequest.java
 * 설명: 경매 상태 변경을 위한 요청 DTO입니다.
 */
package com.goodsple.features.auction.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "경매 상태 변경 요청 DTO")
public class AuctionStatusUpdateRequest {
    @Schema(description = "변경할 상태 값 (예: cancelled, ended)", requiredMode = Schema.RequiredMode.REQUIRED, example = "cancelled")
    @NotBlank
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}