package com.goodsple.features.exchange.service.Impl;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import com.goodsple.features.exchange.mapper.ExchangePostMapper;
import com.goodsple.features.exchange.service.ExchangePostService;
import com.goodsple.features.exchange.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangePostServiceImpl implements ExchangePostService {

  private final ExchangePostMapper exchangePostMapper;
  private final ImageUploadService imageUploadService; // S3 삭제를 위해 추가

  @Override
  @Transactional
  public Long createPost(ExchangePostDto post, Long userId) {

    // 카테고리 유효성 검증
    if (post.getThirdCateId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리 선택은 필수입니다.");
    }

// 거래 방식에 따른 유효성 검증
    if ("DELIVERY".equals(post.getPostTradeType()) || "BOTH".equals(post.getPostTradeType())) {
      if (post.getDeliveryPriceNormal() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "택배 거래 시 일반 택배비는 필수입니다.");
      }
      // 반값택배 옵션과 금액이 선택된 경우에만 유효성 검사
      if (StringUtils.hasText(post.getHalfDeliveryType()) && post.getDeliveryPriceHalf() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "반값 택배비는 필수입니다.");
      }
    } else if ("DIRECT".equals(post.getPostTradeType())) {
      // 직거래만 선택한 경우 배송비 및 반값택배 정보는 저장하지 않음
      post.setDeliveryPriceNormal(null);
      post.setDeliveryPriceHalf(null);
      post.setHalfDeliveryType(null);
    }

    exchangePostMapper.insertExchangePost(post, userId);
    Long newPostId = post.getExchangePostId();

    if (post.getImageUrls() != null && !post.getImageUrls().isEmpty()) {
      List<String> imageUrls = post.getImageUrls();
      for (int i = 0; i < imageUrls.size(); i++) {
        exchangePostMapper.insertExchangePostImage(newPostId, imageUrls.get(i), i);
      }
    }

    return newPostId;
  }

  @Override
  @Transactional
  public void updatePost(Long postId, ExchangePostDto post, Long userId) {

    Optional<Long> postUserId = exchangePostMapper.findUserIdByPostId(postId);
    if (!postUserId.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }
    if (!postUserId.get().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글을 수정할 권한이 없습니다.");
    }

    // 기존 이미지 URL을 DB에서 조회
    List<String> existingImageUrls = exchangePostMapper.findImageUrlsByPostId(postId);

    // S3에서 기존 이미지 삭제
    if (existingImageUrls != null && !existingImageUrls.isEmpty()) {
      imageUploadService.deleteImagesFromS3(existingImageUrls);
    }

    // 게시글 업데이트
    exchangePostMapper.updateExchangePost(postId, post);

    // DB의 이미지 정보 삭제 후 새로 삽입
    exchangePostMapper.deleteExchangePostImages(postId);
    if (post.getImageUrls() != null && !post.getImageUrls().isEmpty()) {
      List<String> imageUrls = post.getImageUrls();
      for (int i = 0; i < imageUrls.size(); i++) {
        exchangePostMapper.insertExchangePostImage(postId, imageUrls.get(i), i);
      }
    }
  }

  @Override
  @Transactional
  public void deletePost(Long postId, Long userId) {

    Optional<Long> postUserId = exchangePostMapper.findUserIdByPostId(postId);
    if (!postUserId.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }
    if (!postUserId.get().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글을 삭제할 권한이 없습니다.");
    }

    // 삭제할 이미지 URL을 DB에서 조회
    List<String> imageUrls = exchangePostMapper.findImageUrlsByPostId(postId);

    // 게시글 이미지 DB 정보 먼저 삭제 (외래키 제약조건 위반 방지)
    exchangePostMapper.deleteExchangePostImages(postId);

    // 게시글 삭제
    exchangePostMapper.deleteExchangePost(postId);

    // S3에서 이미지 삭제 (가장 마지막에 수행)
    if (imageUrls != null && !imageUrls.isEmpty()) {
      imageUploadService.deleteImagesFromS3(imageUrls);
    }
  }

}
