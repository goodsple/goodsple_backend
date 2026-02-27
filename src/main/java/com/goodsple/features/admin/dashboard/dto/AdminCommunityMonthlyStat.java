package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminCommunityMonthlyStat {
    private String month;
    private String roomId;
    private Long activeUsers;
}