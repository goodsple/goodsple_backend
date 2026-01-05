package com.goodsple.features.postlist.controller;

import com.goodsple.features.postlist.dto.PostFilterDto;
import com.goodsple.features.postlist.dto.PostListDto;
import com.goodsple.features.postlist.dto.ThirdIdsRequest;
import com.goodsple.features.postlist.mapper.PostListMapper;
import com.goodsple.features.postlist.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostListController {

  private final PostListService postListService;

  // 글 목록 조회
  @GetMapping
  public List<PostListDto> getAllPosts() {
    return postListService.getAllPosts();
  }

  @GetMapping("/by-category")
  public ResponseEntity<List<PostListDto>> getPostsByCategory(@RequestParam Long categoryId) {
    return ResponseEntity.ok(postListService.getPostsByCategory(categoryId));
  }

  @PostMapping("/filter")
  public ResponseEntity<List<PostListDto>> filterPosts(@RequestBody PostFilterDto filterDto) {
    return ResponseEntity.ok(postListService.getPostsBySecondAndThird(filterDto));
  }


}


