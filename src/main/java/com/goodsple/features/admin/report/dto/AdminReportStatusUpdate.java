package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "관리자 신고 상태 변경 요청 DTO")
public class AdminReportStatusUpdate {
    @Schema(description = "변경할 상태", example = "resolved", allowableValues = {"pending","processing","resolved","rejected"})
    private String status;

    @Schema(description = "조치 내용(선택)", example = "게시글 블라인드 및 경고 1회")
    private String actionTaken;
}
