package com.goodsple.features.myexchange.service.Impl;

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
  public void deletePost(Long postId, Long userId) {
    int deleted = myExchangePostMapper.deletePost(postId, userId);
    if (deleted != 1) {
      throw new RuntimeException("삭제 실패 또는 권한 없음");
    }
  }

  @Override
  public void updatePost(Long postId, Long userId, MyExchangePostUpdateDto updateDto) {
    int updated = myExchangePostMapper.updatePost(postId, userId, updateDto);
    if (updated != 1) {
      throw new RuntimeException("거래글 수정 실패 또는 권한 없음");
    }
  }
}
