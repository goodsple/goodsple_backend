package com.goodsple.features.myexchange.service.Impl;

import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyCompletedExchangeDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.dto.MyExchangePostUpdateDto;
import com.goodsple.features.myexchange.mapper.MyExchangePostMapper;
import com.goodsple.features.myexchange.service.MyExchangePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyExchangePostServiceImpl implements MyExchangePostService {

  private final MyExchangePostMapper myExchangePostMapper;


  @Override
  public List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size) {
    int offset = (page - 1) * size;
    return myExchangePostMapper.selectMyExchangePosts(userId, status, offset, size);
  }

  @Override
  public int getMyPostsCount(Long userId, String status) {
    return myExchangePostMapper.countMyExchangePosts(userId, status);
  }

  @Override
  public void updatePostStatus(Long postId, Long userId, String status) {
    int updated = myExchangePostMapper.updatePostStatus(postId, userId, status);
    if (updated != 1) {
      throw new RuntimeException("거래상태 업데이트 실패 또는 권한 없음");
    }
  }

  @Override
  public List<ChatUserResponseDto> getChatUsers(Long postId, Long sellerId) {

    // 🔒 권한 체크: 이 글의 판매자인지
    boolean isOwner = myExchangePostMapper.isPostOwner(postId, sellerId);
    if (!isOwner) {
      throw new RuntimeException("권한 없음");
    }

    // sellerId = 현재 로그인한 사용자 = currentUserId
    return myExchangePostMapper.selectChatUsersByPostId(postId, sellerId);
  }


  @Override
  public void selectBuyer(Long postId, Long sellerId, Long buyerId) {

    // 1. 검증
    if (sellerId.equals(buyerId)) {
      throw new IllegalArgumentException("본인을 거래상대로 선택할 수 없습니다.");
    }

    // 2. DB 업데이트
    int updated = myExchangePostMapper.updateBuyer(postId, sellerId, buyerId);

    // 3. 결과 검증
    if (updated != 1) {
      throw new RuntimeException("거래상대 지정 실패 또는 권한 없음");
    }


  }


  @Override
  public List<MyCompletedExchangeDto> getMyCompletedExchangeHistory(Long userId) {
    return myExchangePostMapper.selectMyCompletedExchangeHistory(userId);
  }



}
