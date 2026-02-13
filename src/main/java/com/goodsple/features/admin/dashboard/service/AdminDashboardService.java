package com.goodsple.features.admin.dashboard.service;

import com.goodsple.features.admin.dashboard.dto.AdminReportStatsResponse;
import com.goodsple.features.admin.dashboard.dto.AdminUserStatsResponse;
import com.goodsple.features.admin.dashboard.mapper.AdminDashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AdminDashboardMapper mapper;

    @Transactional(readOnly = true)
    public AdminUserStatsResponse getUserStats(int months) {
        AdminUserStatsResponse res = new AdminUserStatsResponse();
        res.setSummary(mapper.selectUserSummary());
        res.setMonthlyStats(mapper.selectUserMonthlyStats(months));
        return res;
    }

    @Transactional(readOnly = true)
    public AdminReportStatsResponse getReportStats(int months) {
        AdminReportStatsResponse res = new AdminReportStatsResponse();
        res.setSummary(mapper.selectReportSummary());
        res.setMonthlyStats(mapper.selectReportMonthlyStats(months));
        return res;
    }
}
