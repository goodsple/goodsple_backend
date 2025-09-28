package com.goodsple.features.admin.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "신고 사유 DTO")
public class ReportReason {
    @Schema(description = "사유 ID", example = "3")
    private Long reasonId;

    @Schema(description = "사유 텍스트", example = "부적절한 게시글 또는 사진")
    private String reasonText;
}
