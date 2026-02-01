package com.goodsple.features.review.dto;

import lombok.Data;

@Data
public class ReviewInsertParam {
  private Long reviewId;
  private Long writerId;
  private Long reviewTargetUserId;
  private Long exchangePostId;
  private Integer rating;
  private String content;
}
