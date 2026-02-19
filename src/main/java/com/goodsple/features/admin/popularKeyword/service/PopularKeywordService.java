package com.goodsple.features.admin.popularKeyword.service;

import com.goodsple.features.admin.popularKeyword.dto.PopularKeywordDTO;
import com.goodsple.features.admin.popularKeyword.mapper.AdminPopularKeywordMapper;
import com.goodsple.features.popular.mapper.PopularKeywordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PopularKeywordService {

  private final AdminPopularKeywordMapper mapper;

  public PopularKeywordService(AdminPopularKeywordMapper mapper) {
    this.mapper = mapper;
  }

  // 전체 조회
  public List<PopularKeywordDTO> getAllKeywords() {
    return mapper.selectAllKeywords();
  }

  // 상태 변경
  @Transactional
  public void updateStatus(Long keywordId, String status) {
    int updated = mapper.updateKeywordStatus(keywordId, status);
    if (updated == 0) {
      throw new IllegalArgumentException("존재하지 않는 키워드 ID: " + keywordId);
    }
  }

}
