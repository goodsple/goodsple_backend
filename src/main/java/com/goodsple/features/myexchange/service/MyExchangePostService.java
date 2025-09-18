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

  // 내 거래글 목록 조회
  public List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size) {
    int offset = (page - 1) * size;
    return myExchangePostMapper.selectMyExchangePosts(userId, status, offset, size);
  }

  public int getMyPostsCount(Long userId, String status) {
    return myExchangePostMapper.countMyExchangePosts(userId, status);
  }


  // 거래상태 변경
  public void updatePostStatus(Long postId, Long userId, String status) {
    int updated = myExchangePostMapper.updatePostStatus(postId, userId, status);
    if (updated != 1) {
      throw new RuntimeException("거래상태 업데이트 실패 또는 권한 없음");
    }
  }

  // 거래글 삭제
  public void deletePost(Long postId, Long userId) {
    int deleted = myExchangePostMapper.deletePost(postId, userId);
    if (deleted != 1) {
      throw new RuntimeException("삭제 실패 또는 권한 없음");
    }
  }

}
