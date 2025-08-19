package com.goodsple.features.exchange.service.Impl;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import com.goodsple.features.exchange.mapper.ExchangePostMapper;
import com.goodsple.features.exchange.service.ExchangePostService;
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
    // 수정 시에도 DTO에 포함된 모든 필드를 업데이트
    exchangePostMapper.updateExchangePost(postId, post);
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
    exchangePostMapper.deleteExchangePost(postId);
  }

}
