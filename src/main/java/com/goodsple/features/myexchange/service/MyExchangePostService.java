package com.goodsple.features.myexchange.service;

import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.dto.MyExchangePostUpdateDto;

import java.util.List;

public interface MyExchangePostService {
  List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size);
  int getMyPostsCount(Long userId, String status);

  void updatePostStatus(Long postId, Long userId, String status);
//  void deletePost(Long postId, Long userId);
//
//  // 거래글 수정 추가
//  void updatePost(Long postId, Long userId, MyExchangePostUpdateDto updateDto);
}
