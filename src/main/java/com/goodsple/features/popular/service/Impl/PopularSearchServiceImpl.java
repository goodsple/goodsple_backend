package com.goodsple.features.popular.service.Impl;

import com.goodsple.features.popular.dto.PopularKeywordDTO;
import com.goodsple.features.popular.mapper.PopularKeywordMapper;
import com.goodsple.features.popular.service.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularSearchServiceImpl implements PopularSearchService {

  private final PopularKeywordMapper mapper;
  private final StringRedisTemplate redisTemplate;

  private static final String REDIS_KEY = "popular_search";

  @Override
  public List<PopularKeywordDTO> getTopKeywords(int limit) {
    // Redis에서 점수 순으로 top N 키워드 가져오기
    List<String> top = redisTemplate.opsForZSet()
        .reverseRange(REDIS_KEY, 0, limit - 1)
        .stream().toList();

    // DB에서 해당 키워드 정보 조회
    return mapper.selectTopKeywords(limit);
  }

  @Override
  public void recordSearch(String keyword) {
    // Redis ZSet 점수 증가
    redisTemplate.opsForZSet().incrementScore(REDIS_KEY, keyword, 1);

    // DB 업데이트
    int updated = mapper.incrementSearchCount(keyword);
    if (updated == 0) {
      mapper.insertKeyword(keyword);
    }
  }
}
