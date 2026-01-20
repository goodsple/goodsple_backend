package com.goodsple.features.myexchange.service;

import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyCompletedExchangeDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;

import java.util.List;

public interface MyExchangePostService {
  List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size);
  int getMyPostsCount(Long userId, String status);

  void updatePostStatus(Long postId, Long userId, String status);

  List<ChatUserResponseDto> getChatUsers(Long postId, Long sellerId);

  void selectBuyer(Long postId, Long sellerId, Long buyerId);

  List<MyCompletedExchangeDto> getMyCompletedExchangeHistory(Long userId);

}
