package com.goodsple.features.review.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReviewSummaryDto {
  private Long id;
  private Long exchangePostId;
  private String postTitle;
  private String date;
  private Integer rating;
  private String content;
  private String thumbnail;
  private List<String> images;
}
