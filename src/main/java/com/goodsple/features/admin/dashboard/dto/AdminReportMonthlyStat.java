package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminReportMonthlyStat {
    private String month; // YY.MM
    private Long reported;
    private Long resolved;
    private Long resolveRate; // percent
    private Long unresolved;
}
