package com.goodsple.features.exchange.mapper;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ExchangePostMapper {

  // 게시글 등록
  void insertExchangePost(Map<String, Object> param);
  // 게시글 수정
  void updateExchangePost(Map<String, Object> param);
  // 게시글 삭제
  void deleteExchangePost(Long exchangePostId);

  // 게시글 조회
  ExchangePostDto findPostById(Long postId);

  Long findUserIdByPostId(@Param("postId") Long postId);

  // 게시글 이미지 등록
  void insertExchangePostImage(@Param("exchangePostId") Long exchangePostId,
                               @Param("imageUrl") String imageUrl,
                               @Param("sortOrder") int sortOrder);

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
