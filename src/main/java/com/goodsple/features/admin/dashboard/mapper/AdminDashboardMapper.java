package com.goodsple.features.admin.dashboard.mapper;

import com.goodsple.features.admin.dashboard.dto.*;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminDashboardMapper {
    AdminUserStatsSummary selectUserSummary();
    List<AdminUserMonthlyStat> selectUserMonthlyStats(@Param("months") int months);

    AdminReportStatsSummary selectReportSummary();
    List<AdminReportMonthlyStat> selectReportMonthlyStats(@Param("months") int months);

//  실시간 검색어
    AdminPopularKeywordSummary selectPopularKeywordSummary();
    List<AdminPopularKeywordItem> selectPopularKeywordTop10();

    List<AdminPopularKeywordItem> selectPopularKeywordTop10Realtime();
    int existsTodaySnapshot();

    // 커뮤니티
    List<AdminCommunityMonthlyStat> selectCommunityMonthlyStats(@Param("months") int months);

    // 경매
    List<AdminAuctionStat> selectRecentAuctionStats();

}
