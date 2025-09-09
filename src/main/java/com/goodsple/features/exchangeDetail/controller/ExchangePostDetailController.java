package com.goodsple.features.exchangeDetail.controller;

import com.goodsple.features.exchangeDetail.dto.PostDetailDto;
import com.goodsple.features.exchangeDetail.service.ExchangePostDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "교환 게시글 상세보기", description = "게시글 상세 정보 조회 및 상태 변경 API")
public class ExchangePostDetailController {

  private final ExchangePostDetailService service;

  @GetMapping("/{postId}")
  @Operation(summary = "게시글 상세 조회", description = "게시글 ID로 상세 정보 조회")
  public PostDetailDto getPostDetail(@PathVariable Long postId) {
    return service.getPostDetail(postId);
  }

  @PutMapping("/{postId}/status")
  @Operation(summary = "게시글 거래상태 변경", description = "작성자만 거래상태 변경 가능")
  public void updatePostStatus(@PathVariable Long postId, @RequestBody StatusRequest request) {
    service.updatePostStatus(postId, request.getStatus());
  }

  @Data
  public static class StatusRequest {
    private String status;
  }

}
