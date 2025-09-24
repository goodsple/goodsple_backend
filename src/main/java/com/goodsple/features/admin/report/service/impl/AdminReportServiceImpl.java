package com.goodsple.features.admin.report.service.impl;

import com.goodsple.features.admin.report.dto.*;
import com.goodsple.features.admin.report.mapper.AdminReportMapper;
import com.goodsple.features.admin.report.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportServiceImpl implements AdminReportService {

    private final AdminReportMapper mapper;

    @Override
    public AdminReportList getReportList(AdminReportSearchCond cond) {
        int page = cond.getPage() == null ? 0 : cond.getPage();
        int size = cond.getSize() == null ? 20 : cond.getSize();
        cond.setPage(page);
        cond.setSize(size);

        long total = mapper.countReportList(cond);
        List<AdminReportListItem> items = total > 0
                ? mapper.selectReportList(cond)
                : Collections.emptyList();

        return AdminReportList.builder()
                .items(items)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public AdminReportDetail getReportDetail(Long reportId) {
        AdminReportDetail detail = mapper.selectReportDetail(reportId);
        if (detail == null) {
            throw new NoSuchElementException("Report not found: " + reportId);
        }
        detail.setReasons(mapper.selectReasonsByReportId(reportId));
        return detail;
    }

    @Override
    @Transactional
    public void updateStatus(Long reportId, AdminReportStatusUpdate request) {
        // (선택) 상태 유효성 간단 체크
        if (request.getStatus() == null ||
                !Set.of("pending","processing","resolved","rejected").contains(request.getStatus().toLowerCase())) {
            throw new IllegalArgumentException("Invalid status: " + request.getStatus());
        }

        String status = request.getStatus().toLowerCase(); // 정규화
        String actionTaken = request.getActionTaken();      // 대문자여도 OK (XML에서 lower 처리)

        int updated = mapper.updateReportStatus(reportId, status, actionTaken);
        if (updated == 0) {
            throw new NoSuchElementException("Report not found: " + reportId);
        }
    }
}
