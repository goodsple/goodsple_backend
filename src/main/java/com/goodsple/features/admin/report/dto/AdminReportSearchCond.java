package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "관리자 신고 검색 조건 DTO")
public class AdminReportSearchCond {
    @Schema(description = "신고 ID", example = "1001")
    private Long reportId;

    @Schema(description = "신고 상태 목록", example = "[\"pending\", \"processing\", \"resolved\", \"rejected\"]")
    private List<String> statuses;

    @Schema(description = "타겟 타입 목록", example = "[\"user\", \"post\", \"review\", \"chat_message\"]")
    private List<String> targetTypes;

    @Schema(description = "신고자 ID", example = "17")
    private Long reporterId;

    @Schema(description = "피신고자 ID", example = "23")
    private Long targetUserId;

    @Schema(description = "타겟 엔티티 ID", example = "2345")
    private Long targetId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "신고일 시작(포함)", example = "2025-01-01")
    private LocalDate createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "신고일 끝(포함)", example = "2025-12-31")
    private LocalDate createdTo;

    @Schema(description = "키워드(사유/설명/닉네임 등)", example = "사기")
    private String keyword;

    @Schema(description = "페이지 번호(0-base)", example = "0")
    private Integer page;

    @Schema(description = "페이지 크기", example = "20")
    private Integer size;

    /* ===== 검색 전처리용(서비스에서 세팅) ===== */

    @Schema(description = "조치 필터(ENUM 라벨)", example = "[\"warning\",\"rejected\"]")
    private List<String> actions;

    @Schema(description = "조치 키워드 유추값 (warning|rejected|suspend_3d|permanent_ban)", example = "suspend_3d")
    private String actionSearch;

    @Schema(description = "키워드가 숫자면 ID 매칭용으로 사용 (report_id/target_id 비교)", example = "1234")
    private Long keywordAsLong;
}
