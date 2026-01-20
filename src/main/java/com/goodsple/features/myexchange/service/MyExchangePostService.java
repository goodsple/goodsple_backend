package com.goodsple.features.myexchange.service;

import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyCompletedExchangeDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;

import java.util.List;

public interface MyExchangePostService {

  // 내가 작성한 교환게시글 목록 조회 (거래상태 필터링, 페이징처리)
  List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size);

  // 내가 작성한 교환게시글 전체 개수 조회
  int getMyPostsCount(Long userId, String status);

  // 교환게시글 거래 상태 변경
  void updatePostStatus(Long postId, Long userId, String status);

  // 특정 거래글에 대해 채팅을 진행한 사용자 조회 (거래완료 후 거래 상대방 지정 용도)
  List<ChatUserResponseDto> getChatUsers(Long postId, Long sellerId);

  // 거래완료된 게시글에 대해 실제 거래 상대 지정
  void selectBuyer(Long postId, Long sellerId, Long buyerId);

  // 내가 참여한 거래완료 내역 조회
  List<MyCompletedExchangeDto> getMyCompletedExchangeHistory(Long userId);

}
