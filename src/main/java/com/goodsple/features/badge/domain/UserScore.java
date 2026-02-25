package com.goodsple.features.badge.domain;

import lombok.Getter;

@Getter
public class UserScore {
    private Long userId;
    private int trustScore;
    private int reviewScore;
    private int tradeScore;
    private int penaltyScore;
}

