package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminPopularKeywordItem {

  private String keyword;
  private int searchCount;
  private int rank;
  private Integer rankChange;
  private Boolean isNew;

}
