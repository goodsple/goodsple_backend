package com.goodsple.features.postlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 필터용 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterDto {

  private Long firstCateId;
  private List<Long> secondIds;
  private List<Long> thirdIds;

}
