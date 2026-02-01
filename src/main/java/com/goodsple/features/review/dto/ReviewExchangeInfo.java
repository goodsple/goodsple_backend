package com.goodsple.features.review.dto;

import lombok.Data;

@Data
public class ReviewExchangeInfo {
  private Long exchangePostId;
  private Long sellerId;
  private Long buyerId;
  private String status;
  private String postTitle;
}
