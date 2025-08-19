package com.goodsple.features.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCategoryDto {
  private Long firstCateId; // 1차 카테고리 ID
  private String firstCateName; // 1차 카테고리 이름
  private Long secondCateId; // 2차 카테고리 ID
  private String secondCateName; // 2차 카테고리 이름
  private Long thirdCateId; // 3차 카테고리 ID
  private String thirdCateName; // 3차 카테고리 이름
}
