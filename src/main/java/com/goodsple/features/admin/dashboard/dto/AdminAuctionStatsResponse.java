package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminAuctionStatsResponse {

    private List<AdminAuctionStat> auctionStats;
}
