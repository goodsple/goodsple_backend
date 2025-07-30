package com.goodsple.features.report.mapper;

import com.goodsple.features.report.dto.request.Report;
import com.goodsple.features.report.dto.request.ReportReason;
import com.goodsple.features.report.dto.request.ReportReasonMapping;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 신고 및 사유 관련 모든 DB 작업을 처리하는 단일 Mapper
 */
@Mapper
public interface ReportMapper {

    // Report
    int insertReport(Report report);
    Report selectReportById(@Param("reportId") Long reportId);

    // 추가: report 목록, 상태 업데이트 등 메서드 정의 가능

    // == ReportReason 관련 ==
    int insertReason(ReportReason reason);
    List<ReportReason> selectAllReasons();

    // == ReportReasonMapping 관련 ==
    int insertReasonMapping(ReportReasonMapping mapping);
    List<Long> selectReasonIdsByReport(@Param("reportId") Long reportId);
}
