package com.goodsple.features.report.service;

import com.goodsple.features.report.dto.request.Report;
import com.goodsple.features.report.dto.request.ReportReason;

import java.util.List;
/**
 * Report 관련 비즈니스 로직 인터페이스
 */
public interface ReportService {

    /**
     * 신고 등록
     */
    void createReport(Report report, List<Long> reasonIds);

    /**
     * ID로 신고 조회
     */
    Report getReportById(Long reportId);

    /**
     * 모든 신고 사유 조회
     */
    List<ReportReason> getAllReasons();

    /**
     * 특정 대상에 대한 신고 여부 확인
     */
    boolean hasReported(Long reporterId, String targetType, Long targetId);

    /**
     * 신고 사유 등록
     */
//    void addReason(ReportReason reason);

}
