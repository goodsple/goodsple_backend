package com.goodsple.features.exchange.controller;

import com.goodsple.features.exchange.dto.PostCategoryDto;
import com.goodsple.features.exchange.service.PostCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상품 카테고리", description = "상품 카테고리 조회 API")
@RestController
@RequestMapping("/api/post-categories")
@RequiredArgsConstructor
public class PostCategoryController {

  private final PostCategoryService postCategoryService;

  @Operation(summary = "전체 3차 상품 카테고리 목록 조회", description = "게시글 등록에 사용되는 전체 카테고리 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<PostCategoryDto>> getAllCategories() {
    List<PostCategoryDto> categories = postCategoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
}
