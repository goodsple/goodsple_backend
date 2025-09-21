package com.goodsple.features.exchange.service;

import com.goodsple.features.exchange.dto.ExchangePostDto;

public interface ExchangePostService {

  Long createPost(ExchangePostDto post, Long userId);

  void updatePost(Long postId, ExchangePostDto post, Long userId);

  void deletePost(Long postId, Long userId);

  ExchangePostDto getPost(Long postId);

}
