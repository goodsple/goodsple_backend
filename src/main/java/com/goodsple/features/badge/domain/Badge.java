package com.goodsple.features.badge.domain;

import lombok.Getter;

@Getter
public class Badge {
    private Long badgeId;
    private String badgeName;
    private String badgeImageUrl;
    private int minScore;
    private int badgeLevel;
}

