package com.goodsple.features.postlist.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostFilterDto {

  private List<Long> secondIds;
  private List<Long> thirdIds;

}
