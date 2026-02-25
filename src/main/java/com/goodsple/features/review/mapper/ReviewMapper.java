package com.goodsple.features.review.mapper;

import com.goodsple.features.review.dto.*;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
  int countByWriterAndPost(@Param("writerId") Long writerId,
                           @Param("exchangePostId") Long exchangePostId);

  ReviewExchangeInfo selectExchangeInfo(@Param("exchangePostId") Long exchangePostId);

  void insertReview(ReviewInsertParam param);

  void insertReviewImage(@Param("reviewId") Long reviewId,
                         @Param("imageUrl") String imageUrl,
                         @Param("orderIndex") int orderIndex);

  List<ReviewSummaryDto> selectWrittenReviews(@Param("userId") Long userId);

  List<ReviewSummaryDto> selectReceivedReviews(@Param("userId") Long userId);

  List<String> selectReviewImages(@Param("reviewId") Long reviewId);

  ReviewDetailDto selectReviewDetail(@Param("reviewId") Long reviewId);

  List<com.goodsple.features.review.dto.ReviewPostItemDto> selectReviewsByPost(
      @Param("exchangePostId") Long exchangePostId);

  ReviewAuthInfo selectReviewAuth(@Param("reviewId") Long reviewId);

  int updateReview(@Param("reviewId") Long reviewId,
                   @Param("writerId") Long writerId,
                   @Param("rating") Integer rating,
                   @Param("content") String content);

  int deleteReview(@Param("reviewId") Long reviewId,
                   @Param("writerId") Long writerId);

  void deleteReviewImages(@Param("reviewId") Long reviewId);

  // 등급 점수 계산을 위한 로직
  ReviewScoreInfo selectReviewScoreInfo(Long reviewId);

}
