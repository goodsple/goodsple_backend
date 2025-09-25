package com.goodsple.features.admin.report.mapper;

import com.goodsple.features.admin.report.dto.AdminReportDetail;
import com.goodsple.features.admin.report.dto.AdminReportListItem;
import com.goodsple.features.admin.report.dto.AdminReportSearchCond;
import com.goodsple.features.admin.report.dto.ReportReason;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminReportMapper {
    // 목록 + 검색 + 페이징
    List<AdminReportListItem> selectReportList(@Param("cond") AdminReportSearchCond cond);

    // 총 건수
    long countReportList(@Param("cond") AdminReportSearchCond cond);

    // 상세 (사유는 별도 쿼리)
    AdminReportDetail selectReportDetail(@Param("reportId") Long reportId);

    // 상세용 사유 목록
    List<ReportReason> selectReasonsByReportId(@Param("reportId") Long reportId);

    // 상태 변경(처리/반려 등)
    int updateReportStatus(@Param("reportId") Long reportId,
                           @Param("status") String status,
                           @Param("actionTaken") String actionTaken);
}
