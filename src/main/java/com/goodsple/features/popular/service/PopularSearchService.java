package com.goodsple.features.popular.service;

import com.goodsple.features.popular.dto.PopularKeywordDTO;

import java.util.List;

public interface PopularSearchService {

  List<PopularKeywordDTO> getTopKeywords(int limit);
  void recordSearch(String keyword);

}
