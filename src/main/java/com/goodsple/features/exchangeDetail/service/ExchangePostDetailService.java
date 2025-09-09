package com.goodsple.features.exchangeDetail.service;

import com.goodsple.features.exchangeDetail.dto.PostDetailDto;
import com.goodsple.features.exchangeDetail.mapper.ExchangePostDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangePostDetailService {

  private final ExchangePostDetailMapper mapper;

  public PostDetailDto getPostDetail(Long postId) {
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
