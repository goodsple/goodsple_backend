package com.goodsple.features.report.service.impl;

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
        return Arrays.stream(ReportReasonType.values())
                .map(e -> new ReportReason(null, e.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public void addReason(ReportReason reason) {
        reportMapper.insertReason(reason);
    }
}
