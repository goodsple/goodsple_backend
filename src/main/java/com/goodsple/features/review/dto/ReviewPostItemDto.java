package com.goodsple.features.review.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReviewPostItemDto {
  private Long reviewId;
  private Long writerId;
  private String writerNickname;
  private String writerProfileImage;
  private Integer rating;
  private String content;
  private String createdAt;
  private List<String> images;
}
