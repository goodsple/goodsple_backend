package com.goodsple.features.exchange.mapper;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ExchangePostMapper {

  // 게시글 등록
  void insertExchangePost(@Param("post") ExchangePostDto post, @Param("userId") Long userId);
  // 게시글 수정
  void updateExchangePost(@Param("postId") Long postId, @Param("post") ExchangePostDto post);
  // 게시글 삭제
  void deleteExchangePost(Long exchangePostId);

  // 게시글 존재 여부 및 작성자 ID 조회 (수정/삭제 권한 확인용)
  @Select("SELECT user_id FROM exchange_post WHERE exchange_post_id = #{postId}")
  Optional<Long> findUserIdByPostId(Long postId);

  // 새롭게 추가된 이미지 관련 메서드
  /**
   * 게시글 이미지를 데이터베이스에 삽입합니다.
   * @param exchangePostId 게시글 ID
   * @param imageUrl 이미지 URL
   * @param sortOrder 이미지 순서
   */
  void insertExchangePostImage(@Param("exchangePostId") Long exchangePostId, @Param("imageUrl") String imageUrl, @Param("sortOrder") int sortOrder);

  /**
   * 특정 게시글 ID에 해당하는 모든 이미지를 삭제합니다.
   * @param exchangePostId 게시글 ID
   */
  void deleteExchangePostImages(@Param("exchangePostId") Long exchangePostId);

  /**
   * 특정 게시글 ID에 해당하는 이미지 URL 목록을 조회합니다.
   * @param exchangePostId 게시글 ID
   * @return 이미지 URL 리스트
   */
  List<String> findImageUrlsByPostId(@Param("exchangePostId") Long exchangePostId);

}
