package com.goodsple.features.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewScoreInfo {

    private Long reviewId;

    private Long targetUserId;

    private int rating;

    private String content;

    private int imageCount;
}
