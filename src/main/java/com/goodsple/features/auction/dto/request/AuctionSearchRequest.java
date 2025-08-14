/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/request/AuctionSearchRequest.java
 * 설명: 경매 목록 검색 조건을 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Schema(description = "경매 목록 검색 조건 DTO")
@Data // Getter, Setter, ToString 등을 자동 생성
public class AuctionSearchRequest {
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private int page = 0;

    @Schema(description = "페이지 크기", example = "10")
    private int size = 10;

    @Schema(description = "상품명 검색어", example = "세븐틴")
    private String productName;

    @Schema(description = "경매 상태 필터 (scheduled, active, ended, cancelled)", example = "active")
    private String status;

    @Schema(description = "검색 시작 날짜 (YYYY-MM-DD)", example = "2025-08-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "검색 종료 날짜 (YYYY-MM-DD)", example = "2025-08-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}