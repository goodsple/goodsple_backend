package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminPopularKeywordStatsResponse {

  private AdminPopularKeywordSummary summary;
  private List<AdminPopularKeywordItem> topKeywords;

}
