package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "관리자 신고 목록 아이템 DTO")
public class AdminReportListItem {
    @Schema(description = "신고 ID", example = "1001")
    private Long reportId;

    @Schema(description = "신고 상태", example = "pending")
    private String status;

    @Schema(description = "타겟 타입", example = "post")
    private String targetType;

    @Schema(description = "타겟 ID", example = "2345")
    private Long targetId;

    @Schema(description = "신고자 ID", example = "17")
    private Long reporterId;

    @Schema(description = "신고자 닉네임", example = "레몬사탕")
    private String reporterNickname;

    @Schema(description = "피신고자 ID", example = "23")
    private Long targetUserId;

    @Schema(description = "피신고자 닉네임", example = "허니브레드")
    private String targetNickname;

    @Schema(description = "신고 사유 텍스트 목록", example = "[\"부적절한 게시글 또는 사진\", \"불쾌한 행동 및 언행\"]")
    private List<String> reasons;

    @Schema(description = "신고 생성 시각", example = "2025-08-10T11:20:30")
    private LocalDateTime createdAt;
}
