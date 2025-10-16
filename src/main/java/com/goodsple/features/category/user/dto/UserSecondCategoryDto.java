package com.goodsple.features.category.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSecondCategoryDto {
  private Long id;       // 2차 카테고리 id (선택/삭제 용)
  private String name;   // 2차 카테고리명
}
