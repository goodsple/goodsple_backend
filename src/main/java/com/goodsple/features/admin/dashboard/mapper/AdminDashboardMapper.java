package com.goodsple.features.admin.dashboard.mapper;

import com.goodsple.features.admin.dashboard.dto.AdminReportMonthlyStat;
import com.goodsple.features.admin.dashboard.dto.AdminReportStatsSummary;
import com.goodsple.features.admin.dashboard.dto.AdminUserMonthlyStat;
import com.goodsple.features.admin.dashboard.dto.AdminUserStatsSummary;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminDashboardMapper {
    AdminUserStatsSummary selectUserSummary();
    List<AdminUserMonthlyStat> selectUserMonthlyStats(@Param("months") int months);

    AdminReportStatsSummary selectReportSummary();
    List<AdminReportMonthlyStat> selectReportMonthlyStats(@Param("months") int months);
}
