package com.goodsple.features.exchange.mapper;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ExchangePostMapper {

  // 게시글 등록
  void insertExchangePost(@Param("post") ExchangePostDto post, @Param("userId") Long userId);

  // 게시글 이미지 등록
  void insertExchangePostImage(@Param("exchangePostId") Long exchangePostId, @Param("imageUrl") String imageUrl, @Param("sortOrder") int sortOrder);

  // 게시글 수정
  void updateExchangePost(@Param("postId") Long postId, @Param("post") ExchangePostDto post);

  // 게시글에 연결된 이미지 삭제 (전체 삭제 후 재등록 방식)
  void deleteExchangePostImages(Long exchangePostId);

  // 게시글 삭제
  void deleteExchangePost(Long exchangePostId);

  // 게시글 존재 여부 및 작성자 ID 조회 (수정/삭제 권한 확인용)
  @Select("SELECT user_id FROM exchange_post WHERE exchange_post_id = #{postId}")
  Optional<Long> findUserIdByPostId(Long postId);

}
