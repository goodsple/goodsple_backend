package com.goodsple.features.postlist.service.Impl;

import com.goodsple.features.postlist.dto.PostListDto;
import com.goodsple.features.postlist.mapper.PostListMapper;
import com.goodsple.features.postlist.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
