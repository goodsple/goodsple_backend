package com.goodsple.features.badge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyScoreResponseDto {

    private Long userId;

    private int trustScore;
    private int reviewScore;
    private int penaltyScore;

    private int totalScore;

    private int badgeLevel;
    private String badgeName;
    private String badgeImageUrl;

    private Integer nextMinScore;
    private Integer scoreGap;


}
