package com.goodsple.features.admin.dashboard.service;

import com.goodsple.features.admin.dashboard.dto.AdminCommunityStatsResponse;
import com.goodsple.features.admin.dashboard.dto.AdminPopularKeywordStatsResponse;
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

//  실시간 검색어
    @Transactional(readOnly = true)
    public AdminPopularKeywordStatsResponse getPopularKeywordStats() {

        AdminPopularKeywordStatsResponse res =
            new AdminPopularKeywordStatsResponse();

        boolean exists = mapper.existsTodaySnapshot() > 0;

        res.setSummary(mapper.selectPopularKeywordSummary());

        if (exists) {
            res.setTopKeywords(mapper.selectPopularKeywordTop10());
        } else {
            res.setTopKeywords(mapper.selectPopularKeywordTop10Realtime());
        }

        return res;
    }

    // 커뮤니티
    @Transactional(readOnly = true)
    public AdminCommunityStatsResponse getCommunityStats(int months) {
        AdminCommunityStatsResponse res = new AdminCommunityStatsResponse();
        res.setMonthlyStats(mapper.selectCommunityMonthlyStats(months));
        return res;
    }

}
