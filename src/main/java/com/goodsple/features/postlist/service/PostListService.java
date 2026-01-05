package com.goodsple.features.postlist.service;

import com.goodsple.features.postlist.dto.PostFilterDto;
import com.goodsple.features.postlist.dto.PostListDto;

import java.util.List;

public interface PostListService {

  List<PostListDto> getAllPosts();

  List<PostListDto> getPostsByCategory(Long categoryId);

  List<PostListDto> getPostsBySecondAndThird(PostFilterDto filterDto);

//  List<Long> getSecondIdsByThirdIds(List<Long> thirdIds);

}
