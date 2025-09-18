package com.goodsple.features.exchange.controller;

import com.goodsple.features.exchange.dto.ExchangePostDto;
import com.goodsple.features.exchange.service.ExchangePostService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Tag(name = "교환게시글", description = "교환 게시글 등록, 수정, 삭제 API")
@RestController
@RequestMapping("/api/exchange-posts")
@RequiredArgsConstructor
public class ExchangePostController {

  private final ExchangePostService exchangePostService;

  @Operation(summary = "교환 게시글 등록", description = "새로운 교환 게시글을 등록합니다.")
  @PostMapping
  public ResponseEntity<Map<String, Long>> createExchangePost(
      @Valid @RequestBody ExchangePostDto post,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getUserId();
    Long postId = exchangePostService.createPost(post, userId);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(postId)
        .toUri();

    Map<String, Long> responseBody = Map.of("postId", postId);

    return ResponseEntity.created(location).body(Map.of("postId", postId));
  }

  @Operation(summary = "교환 게시글 수정", description = "기존 교환 게시글의 내용을 수정합니다.")
  @PutMapping("/{postId}")
  public ResponseEntity<Void> updateExchangePost(
      @PathVariable Long postId,
      @Valid @RequestBody ExchangePostDto post,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    Long userId = userDetails.getUserId();
    exchangePostService.updatePost(postId, post, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "교환 게시글 삭제", description = "특정 교환 게시글을 삭제합니다.")
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deleteExchangePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    Long userId = userDetails.getUserId();
    exchangePostService.deletePost(postId, userId);
    return ResponseEntity.noContent().build();
  }

}
