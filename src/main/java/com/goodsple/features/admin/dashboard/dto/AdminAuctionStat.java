package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminAuctionStat {

    private String date;        // 경매 날짜

    private Long bidderCount;   // 고유 입찰자 수
}


