package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminUserStatsSummary {
    private Long totalUsers;
    private Long todayJoined;
    private Long todayWithdrawn;
    private Long todayNet;
}
