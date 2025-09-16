package com.goodsple.features.myexchange.controller;

import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.service.MyExchangePostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/my-exchange-posts")
@Tag(name = "MyExchangePost", description = "내 거래글 관련 API")
@RequiredArgsConstructor
public class MyExchangePostController {

  private final MyExchangePostService myExchangePostService;

  @Operation(summary = "내 거래글 조회", description = "로그인 유저의 거래글 목록과 총 개수를 조회합니다.")
  @GetMapping
  public Map<String, Object> getMyPosts(
      @RequestParam Long userId,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int size
  ) {
    List<MyExchangePostDto> posts = myExchangePostService.getMyPosts(userId, status, page, size);
    int totalCount = myExchangePostService.getMyPostsCount(userId, status);

    Map<String, Object> result = new HashMap<>();
    result.put("posts", posts);
    result.put("totalCount", totalCount);
    return result;
  }


  // 거래상태 변경
  @Operation(summary = "거래상태 변경", description = "내 거래글의 상태를 변경합니다.")
  @PatchMapping("/{postId}/status")
  public void updateStatus(
      @PathVariable Long postId,
      @RequestParam Long userId, // JWT에서 가져올 경우 @RequestHeader로 처리 가능
      @RequestParam String status
  ) {

    myExchangePostService.updatePostStatus(postId, userId, status);
  }



}
