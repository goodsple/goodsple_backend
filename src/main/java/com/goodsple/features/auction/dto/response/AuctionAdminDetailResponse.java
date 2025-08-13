/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/AuctionAdminDetailResponse.java
 * 설명: 관리자용 경매 상세 정보 응답 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "관리자용 경매 상세 조회 응답 DTO")
public class AuctionAdminDetailResponse {
    public Long id;
    public String productName;
    public String description;
    public BigDecimal startPrice;
    public BigDecimal minBidUnit;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public List<String> imageUrls;
    public String status;
}