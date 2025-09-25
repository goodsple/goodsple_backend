package com.goodsple.features.admin.report.service;

import com.goodsple.features.admin.report.dto.AdminReportDetail;
import com.goodsple.features.admin.report.dto.AdminReportList;
import com.goodsple.features.admin.report.dto.AdminReportSearchCond;
import com.goodsple.features.admin.report.dto.AdminReportStatusUpdate;

public interface AdminReportService {
    AdminReportList getReportList(AdminReportSearchCond cond);
    AdminReportDetail getReportDetail(Long reportId);
    void updateStatus(Long reportId, AdminReportStatusUpdate request);
}
