package com.goodsple.features.category.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserThirdCategoryDto {

  private Long id;       // 3차 카테고리 id
  private String name;   // 3차 카테고리명
  private Long secondId; // 연결된 2차 카테고리 id

}
