package com.goodsple.features.admin.dashboard.dto;

import java.util.List;
import lombok.Data;

@Data
public class AdminReportStatsResponse {
    private AdminReportStatsSummary summary;
    private List<AdminReportMonthlyStat> monthlyStats;
}
