package com.goodsple.features.myexchange.service;

import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.mapper.MyExchangePostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyExchangePostService {

  private final MyExchangePostMapper myExchangePostMapper;

  public List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size) {
    int offset = (page - 1) * size;
    return myExchangePostMapper.selectMyExchangePosts(userId, status, offset, size);
  }

  public int getMyPostsCount(Long userId, String status) {
    return myExchangePostMapper.countMyExchangePosts(userId, status);
  }

}
