package com.goodsple.features.admin.dashboard.dto;

import java.util.List;
import lombok.Data;

@Data
public class AdminUserStatsResponse {
    private AdminUserStatsSummary summary;
    private List<AdminUserMonthlyStat> monthlyStats;
}
