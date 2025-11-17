package com.goodsple.features.postlist.mapper;

import com.goodsple.features.postlist.dto.PostFilterDto;
import com.goodsple.features.postlist.dto.PostListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostListMapper {
  List<PostListDto> findAllPosts();

  List<PostListDto> findPostsByCategory(@Param("categoryId") Long categoryId);

  // 2차/3차 카테고리 필터링
  List<PostListDto> findPostsBySecondAndThird(@Param("filter") PostFilterDto filter);

}
