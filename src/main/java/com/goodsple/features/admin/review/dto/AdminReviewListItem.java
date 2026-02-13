package com.goodsple.features.admin.review.dto;

import lombok.Data;

@Data
public class AdminReviewListItem {
    private Long reviewId;
    private String author;
    private String targetUser;
    private String title;
    private String content;
    private Integer reportCount;
    private Integer rating;
    private String status; // NORMAL | BLIND
    private String createdAt;
}
