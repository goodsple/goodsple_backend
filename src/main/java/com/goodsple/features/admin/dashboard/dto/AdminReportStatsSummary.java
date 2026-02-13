package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminReportStatsSummary {
    private Long todayReported;
    private Long todayResolved;
    private Long unresolvedTotal;
}
