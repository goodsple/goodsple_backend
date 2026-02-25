package com.goodsple.features.report.service.impl;

import com.goodsple.features.badge.service.UserScoreService;
import com.goodsple.features.report.dto.request.Report;
import com.goodsple.features.report.dto.request.ReportReason;
import com.goodsple.features.report.dto.request.ReportReasonMapping;
import com.goodsple.features.report.enums.ReportReasonType;
import com.goodsple.features.report.mapper.ReportMapper;
import com.goodsple.features.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ReportService 구현체
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    private final UserScoreService userScoreService;


    @Override
    @Transactional
    public void createReport(Report report, List<Long> reasonIds) {
        // 신고 기본 정보 저장
        reportMapper.insertReport(report);
        // 매핑 테이블에 사유 저장
        for (Long reasonId : reasonIds) {
            ReportReasonMapping mapping = new ReportReasonMapping(report.getReportId(), reasonId);
            reportMapper.insertReasonMapping(mapping);
        }
    }
    @Override
    public Report getReportById(Long reportId) {
        Report report = reportMapper.selectReportById(reportId);
        List<Long> reasonIds = reportMapper.selectReasonIdsByReport(reportId);
        // 필요 시 DTO에 사유 ID 리스트 주입 가능
        return report;
    }

    @Override
    public List<ReportReason> getAllReasons() {
        List<ReportReason> reasons = reportMapper.selectAllReasons();
        if (reasons == null || reasons.isEmpty()) {
            // 기본 사유 시드 (중복은 ON CONFLICT로 무시)
            for (ReportReasonType type : ReportReasonType.values()) {
                ReportReason reason = new ReportReason();
                reason.setReportReasonText(type.getDescription());
                reportMapper.insertReason(reason);
            }
            reasons = reportMapper.selectAllReasons();
        }
        return reasons;
    }

    @Override
    public boolean hasReported(Long reporterId, String targetType, Long targetId) {
        return reportMapper.existsReport(reporterId, targetType, targetId);
    }

//    @Override
//    public void addReason(ReportReason reason) {
//        reportMapper.insertReason(reason);
//    }


    @Transactional
    public void processReport(Long reportId, String actionTaken) {

        Report report = reportMapper.selectReportById(reportId);
        Long targetUserId = report.getReportTargetUserId();

        if ("warning".equalsIgnoreCase(actionTaken)) {
            userScoreService.penaltyReview(targetUserId);
        }

        if ("permanent_ban".equalsIgnoreCase(actionTaken)) {
            userScoreService.penaltyFakePost(targetUserId);
        }

    }


}








