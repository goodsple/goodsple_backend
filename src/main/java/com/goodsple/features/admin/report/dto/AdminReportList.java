package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "관리자 신고 목록 응답 DTO")
public class AdminReportList {
    @Schema(description = "목록 아이템")
    private List<AdminReportListItem> items;

    @Schema(description = "전체 건수", example = "153")
    private long total;

    @Schema(description = "현재 페이지(0-base)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "20")
    private int size;
}
