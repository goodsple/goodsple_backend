package com.goodsple.features.myexchange.controller;

import com.goodsple.features.myexchange.dto.*;
import com.goodsple.features.myexchange.service.MyExchangePostService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  // 거래상대 조회
  @Operation(summary = "거래상대 조회", description = "거래상대를 조회합니다.")
  @GetMapping("/{postId}/chat-users")
  public List<ChatUserResponseDto> getChatUsers(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return myExchangePostService.getChatUsers(postId, user.getUserId());
  }

  // 거래상대 선택
  @Operation(summary = "거래상대 선택", description = "거래완료된 글의 거래상대를 확정합니다.")
  @PostMapping("/{postId}/buyer")
  public void selectBuyer(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails user, // 판매자
      @RequestBody BuyerSelectRequestDto dto
  ) {
    myExchangePostService.selectBuyer
        (
            postId,
            user.getUserId(), // 판매자 = 로그인 유저
            dto.getBuyerId()
        );
  }

  @Operation(summary = "내 거래완료 내역 조회")
  @GetMapping("/completed")
  public List<MyCompletedExchangeDto> getMyCompletedHistory(
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return myExchangePostService.getMyCompletedExchangeHistory(user.getUserId());
  }

}
