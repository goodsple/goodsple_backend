package com.goodsple.features.exchangeDetail.mapper;

import com.goodsple.features.exchangeDetail.dto.PostDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangePostDetailMapper {

  PostDetailDto selectPostDetailById(@Param("postId") Long postId);

  void updatePostStatus(@Param("postId") Long postId, @Param("status") String status);

  List<String> findImageUrlsByPostId(@Param("postId") Long postId);

}
