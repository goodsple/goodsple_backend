package com.goodsple.features.review.service;

import com.goodsple.features.review.dto.ReviewAuthInfo;
import com.goodsple.features.review.dto.ReviewCreateRequest;
import com.goodsple.features.review.dto.ReviewDetailDto;
import com.goodsple.features.review.dto.ReviewExchangeInfo;
import com.goodsple.features.review.dto.ReviewInsertParam;
import com.goodsple.features.review.dto.ReviewSummaryDto;
import com.goodsple.features.review.dto.ReviewUpdateRequest;
import com.goodsple.features.review.mapper.ReviewMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewMapper reviewMapper;

  @Transactional
  public Long createReview(Long userId, ReviewCreateRequest request) {
    ReviewExchangeInfo exchangeInfo = reviewMapper.selectExchangeInfo(request.getExchangePostId());
    if (exchangeInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "거래글을 찾을 수 없습니다.");
    }

    if (!"COMPLETED".equals(exchangeInfo.getStatus())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "거래완료 상태에서만 리뷰를 작성할 수 있습니다.");
    }

    if (exchangeInfo.getBuyerId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "거래 상대가 지정되지 않았습니다.");
    }

    if (!exchangeInfo.getBuyerId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "구매자만 리뷰를 작성할 수 있습니다.");
    }

    int exists = reviewMapper.countByWriterAndPost(userId, request.getExchangePostId());
    if (exists > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 작성한 리뷰가 있습니다.");
    }

    ReviewInsertParam param = new ReviewInsertParam();
    param.setWriterId(userId);
    param.setReviewTargetUserId(exchangeInfo.getSellerId());
    param.setExchangePostId(request.getExchangePostId());
    param.setRating(request.getRating());
    param.setContent(request.getContent());

    reviewMapper.insertReview(param);

    List<String> imageUrls = request.getImageUrls();
    if (imageUrls != null && !imageUrls.isEmpty()) {
      for (int i = 0; i < imageUrls.size(); i++) {
        reviewMapper.insertReviewImage(param.getReviewId(), imageUrls.get(i), i + 1);
      }
    }

    return param.getReviewId();
  }

  @Transactional(readOnly = true)
  public List<ReviewSummaryDto> getWrittenReviews(Long userId) {
    List<ReviewSummaryDto> reviews = reviewMapper.selectWrittenReviews(userId);
    attachImages(reviews);
    return reviews;
  }

  @Transactional(readOnly = true)
  public List<ReviewSummaryDto> getReceivedReviews(Long userId) {
    List<ReviewSummaryDto> reviews = reviewMapper.selectReceivedReviews(userId);
    attachImages(reviews);
    return reviews;
  }

  @Transactional(readOnly = true)
  public ReviewDetailDto getReviewDetail(Long reviewId, Long userId) {
    ReviewAuthInfo auth = reviewMapper.selectReviewAuth(reviewId);
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.");
    }
    if (!userId.equals(auth.getWriterId()) && !userId.equals(auth.getTargetUserId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰를 조회할 권한이 없습니다.");
    }

    ReviewDetailDto detail = reviewMapper.selectReviewDetail(reviewId);
    if (detail == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.");
    }
    detail.setImages(reviewMapper.selectReviewImages(reviewId));
    return detail;
  }

  @Transactional(readOnly = true)
  public List<com.goodsple.features.review.dto.ReviewPostItemDto> getReviewsByPost(Long exchangePostId) {
    List<com.goodsple.features.review.dto.ReviewPostItemDto> reviews =
        reviewMapper.selectReviewsByPost(exchangePostId);
    if (reviews == null || reviews.isEmpty()) {
      return reviews;
    }
    for (com.goodsple.features.review.dto.ReviewPostItemDto review : reviews) {
      List<String> images = reviewMapper.selectReviewImages(review.getReviewId());
      review.setImages(images == null ? Collections.emptyList() : images);
    }
    return reviews;
  }

  @Transactional
  public void updateReview(Long reviewId, Long userId, ReviewUpdateRequest request) {
    ReviewAuthInfo auth = reviewMapper.selectReviewAuth(reviewId);
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.");
    }
    if (!userId.equals(auth.getWriterId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰를 수정할 권한이 없습니다.");
    }

    reviewMapper.updateReview(reviewId, userId, request.getRating(), request.getContent());

    reviewMapper.deleteReviewImages(reviewId);
    List<String> imageUrls = request.getImageUrls();
    if (imageUrls != null && !imageUrls.isEmpty()) {
      for (int i = 0; i < imageUrls.size(); i++) {
        reviewMapper.insertReviewImage(reviewId, imageUrls.get(i), i + 1);
      }
    }
  }

  @Transactional
  public void deleteReview(Long reviewId, Long userId) {
    ReviewAuthInfo auth = reviewMapper.selectReviewAuth(reviewId);
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.");
    }
    if (!userId.equals(auth.getWriterId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰를 삭제할 권한이 없습니다.");
    }

    reviewMapper.deleteReviewImages(reviewId);
    reviewMapper.deleteReview(reviewId, userId);
  }

  private void attachImages(List<ReviewSummaryDto> reviews) {
    if (reviews == null || reviews.isEmpty()) {
      return;
    }
    for (ReviewSummaryDto review : reviews) {
      List<String> images = reviewMapper.selectReviewImages(review.getId());
      review.setImages(images == null ? Collections.emptyList() : images);
    }
  }
}
