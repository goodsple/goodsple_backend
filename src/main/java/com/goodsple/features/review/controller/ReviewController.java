package com.goodsple.features.review.controller;

import com.goodsple.features.review.dto.ReviewCreateRequest;
import com.goodsple.features.review.dto.ReviewDetailDto;
import com.goodsple.features.review.dto.ReviewSummaryDto;
import com.goodsple.features.review.dto.ReviewUpdateRequest;
import com.goodsple.features.review.service.ReviewService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 작성/조회/수정/삭제 API")
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "리뷰 작성", description = "거래완료된 게시글의 구매자만 리뷰를 작성할 수 있습니다.")
  @PostMapping
  public ResponseEntity<Map<String, Long>> createReview(
      @AuthenticationPrincipal CustomUserDetails user,
      @Valid @RequestBody ReviewCreateRequest request
  ) {
    Long reviewId = reviewService.createReview(user.getUserId(), request);
    return ResponseEntity.ok(Map.of("reviewId", reviewId));
  }

  @Operation(summary = "작성한 리뷰 목록 조회")
  @GetMapping("/written")
  public List<ReviewSummaryDto> getWrittenReviews(
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return reviewService.getWrittenReviews(user.getUserId());
  }

  @Operation(summary = "받은 리뷰 목록 조회")
  @GetMapping("/received")
  public List<ReviewSummaryDto> getReceivedReviews(
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return reviewService.getReceivedReviews(user.getUserId());
  }

  @Operation(summary = "리뷰 상세 조회")
  @GetMapping("/{reviewId}")
  public ReviewDetailDto getReviewDetail(
      @PathVariable Long reviewId,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return reviewService.getReviewDetail(reviewId, user.getUserId());
  }

  @Operation(summary = "거래글별 리뷰 목록 조회")
  @GetMapping("/by-post/{exchangePostId}")
  public List<com.goodsple.features.review.dto.ReviewPostItemDto> getReviewsByPost(
      @PathVariable Long exchangePostId
  ) {
    return reviewService.getReviewsByPost(exchangePostId);
  }

  @Operation(summary = "리뷰 수정", description = "작성자만 수정할 수 있습니다.")
  @PutMapping("/{reviewId}")
  public ResponseEntity<Void> updateReview(
      @PathVariable Long reviewId,
      @AuthenticationPrincipal CustomUserDetails user,
      @Valid @RequestBody ReviewUpdateRequest request
  ) {
    reviewService.updateReview(reviewId, user.getUserId(), request);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "리뷰 삭제", description = "작성자만 삭제할 수 있습니다.")
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(
      @PathVariable Long reviewId,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    reviewService.deleteReview(reviewId, user.getUserId());
    return ResponseEntity.noContent().build();
  }
}
