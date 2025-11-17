package com.goodsple.features.postlist.controller;

import com.goodsple.features.postlist.dto.PostFilterDto;
import com.goodsple.features.postlist.dto.PostListDto;
import com.goodsple.features.postlist.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  // 카테고리별 글 조회
//  @GetMapping("/posts")
//  public ResponseEntity<List<PostListDto>> getPostsByCategory(@RequestParam String category) {
//    List<PostListDto> posts = postListService.findPostsByCategory(category);
//    return ResponseEntity.ok(posts);
//  }

  @GetMapping("/by-category")
  public ResponseEntity<List<PostListDto>> getPostsByCategory(@RequestParam Long categoryId) {
    return ResponseEntity.ok(postListService.getPostsByCategory(categoryId));
  }

  // 2차/3차 카테고리 필터
  @PostMapping("/filter")
  public ResponseEntity<List<PostListDto>> filterPosts(@RequestBody PostFilterDto filterDto) {
    List<PostListDto> filteredPosts = postListService.getPostsBySecondAndThird(filterDto);
    return ResponseEntity.ok(filteredPosts);
  }

}


