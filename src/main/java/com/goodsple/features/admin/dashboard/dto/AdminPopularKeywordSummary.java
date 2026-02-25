package com.goodsple.features.admin.dashboard.dto;

import lombok.Data;

@Data
public class AdminPopularKeywordSummary {

  private int totalSearchCount;   // 전체 누적 검색수
  private int visibleKeywordCount; // 노출 키워드 수
  private int blockedKeywordCount; // 차단 키워드 수
  private int totalSearchDiff; // 전일 대비 총검색수 차이

}
