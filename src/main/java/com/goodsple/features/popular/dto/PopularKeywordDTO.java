package com.goodsple.features.popular.dto;

import java.time.OffsetDateTime;

public record PopularKeywordDTO(
  Long keywordId,
  String keyword,
  int searchCount,
  String keywordStatus,
  OffsetDateTime lastRankInTime,
  OffsetDateTime lastAggregatedTime
  )
{}
