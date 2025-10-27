package com.goodsple.features.exchangedetail.service;

import com.goodsple.features.exchangedetail.dto.PostDetailDto;
import com.goodsple.features.exchangedetail.mapper.ExchangePostDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangePostDetailService {

  private final ExchangePostDetailMapper mapper;

  public PostDetailDto getPostDetail(Long postId) {

    // 1. 조회수 증가
    mapper.incrementViewCount(postId);

    // 2. 게시글 상세 조회
    PostDetailDto post = mapper.selectPostDetailById(postId);
    if (post != null) {
      List<String> images = mapper.findImageUrlsByPostId(postId);
      post.setImages(images);
    }
    return post;
  }

  public void updatePostStatus(Long postId, String status) {
    mapper.updatePostStatus(postId, status);
  }

}
