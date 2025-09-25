package com.goodsple.features.postlist.service;

import com.goodsple.features.postlist.dto.PostListDto;

import java.util.List;

public interface PostListService {

  List<PostListDto> getAllPosts();

  List<PostListDto> getPostsByCategory(Long categoryId);

}
