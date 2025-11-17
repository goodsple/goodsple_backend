package com.goodsple.features.exchangedetail.mapper;

import com.goodsple.features.exchangedetail.dto.PostDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangePostDetailMapper {

  PostDetailDto selectPostDetailById(@Param("postId") Long postId);

  // 조회수 증가
  void incrementViewCount(@Param("postId") Long postId);

  void updatePostStatus(@Param("postId") Long postId, @Param("status") String status);

  List<String> findImageUrlsByPostId(@Param("postId") Long postId);

}
