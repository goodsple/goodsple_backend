package com.goodsple.features.admin.popularKeyword.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PopularKeywordDTO {

  private Long keywordId;
  private String keyword;
  private Integer searchCount;
  private String keywordStatus;
  private OffsetDateTime lastRankInTime;
  private OffsetDateTime lastAggregatedTime;
  private Boolean isProhibited;


}
