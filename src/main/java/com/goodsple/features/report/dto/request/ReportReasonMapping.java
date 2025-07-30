package com.goodsple.features.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportReasonMapping {
    private Long reportId;
    private Long reportReasonId;
}
