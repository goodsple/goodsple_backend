package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;
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

    @Schema(description = "신고 사유(콤마로 합쳐진 텍스트)", example = "부적절한 게시글 또는 사진, 사기 의심 거래")
    private String reasonsText;

    @Schema(description = "신고 설명 텍스트")
    private String description;

    @Schema(description = "신고 생성 시각", example = "2025-08-10T11:20:30")
    private OffsetDateTime createdAt;

    @Schema(description = "조치(DB 라벨: warning/rejected/suspend_3d/permanent_ban)")
    private String actionTaken;

    @Schema(description = "처리 시각")
    private OffsetDateTime processedAt;
}
