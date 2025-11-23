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

  List<Long> findSecondIdsByThirdIds(@Param("thirdIds") List<Long> thirdIds);

  List<PostListDto> findPostsBySecondAndThird(@Param("secondIds") List<Long> secondIds,
                                              @Param("thirdIds") List<Long> thirdIds);


}


