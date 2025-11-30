package com.goodsple.features.admin.auction.dto.response;

import lombok.Data;

@Data
public class WinnerInfo {
    private Long userId;
    private String nickname;
    private String phone;
}