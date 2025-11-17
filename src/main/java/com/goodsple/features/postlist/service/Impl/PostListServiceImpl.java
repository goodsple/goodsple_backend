package com.goodsple.features.postlist.service.Impl;

import com.goodsple.features.postlist.dto.PostListDto;
import com.goodsple.features.postlist.mapper.PostListMapper;
import com.goodsple.features.postlist.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostListServiceImpl implements PostListService {

  private final PostListMapper postListMapper;

  @Override
  public List<PostListDto> getAllPosts() {
    return postListMapper.findAllPosts();
  }

  @Override
  public List<PostListDto> getPostsByCategory(Long categoryId) {
    return postListMapper.findPostsByCategory(categoryId);
  }

  @Override
  public List<PostListDto> getPostsByCategoryFilter(List<Long> secondIds, List<Long> thirdIds) {
    // 2차 전체 선택 시 secondIds를 null로 보내서 쿼리에서 무시하도록
    if (secondIds != null && secondIds.contains(0L)) {
      secondIds = null;
    }
    return postListMapper.findPostsByCategoryFilter(secondIds, thirdIds);
  }


}
