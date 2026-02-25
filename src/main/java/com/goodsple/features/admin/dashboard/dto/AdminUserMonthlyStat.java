package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminUserMonthlyStat {
    private String month; // YY.MM
    private Long joined;
    private Long withdrawn;
    private Long net;
}
