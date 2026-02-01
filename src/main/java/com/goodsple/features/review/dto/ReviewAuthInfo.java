package com.goodsple.features.review.dto;

import lombok.Data;

@Data
public class ReviewAuthInfo {
  private Long reviewId;
  private Long writerId;
  private Long targetUserId;
}
