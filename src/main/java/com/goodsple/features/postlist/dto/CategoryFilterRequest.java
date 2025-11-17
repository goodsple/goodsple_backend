package com.goodsple.features.postlist.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryFilterRequest {

  private List<Long> secondIds;
  private List<Long> thirdIds;

}
