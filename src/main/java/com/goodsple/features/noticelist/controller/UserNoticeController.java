package com.goodsple.features.noticelist.controller;

import com.goodsple.features.noticelist.dto.UserNoticeDto;
import com.goodsple.features.noticelist.service.UserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/notices")
@RequiredArgsConstructor
@Tag(name = "UserNotice", description = "사용자 공지사항 API")
public class UserNoticeController {

  private final UserNoticeService userNoticeService;

  @Operation(summary = "공지사항 목록 조회", description = "페이지와 사이즈를 지정하여 공지사항 목록을 조회합니다.")
  @GetMapping
  public List<UserNoticeDto> getNotices(
      @Parameter(description = "페이지 번호 (기본값 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "페이지 사이즈 (기본값 10)") @RequestParam(defaultValue = "10") int size
  ) {
    return userNoticeService.getNotices(page, size);
  }

  @Operation(summary = "공지사항 총 개수 조회", description = "전체 공지사항 개수를 반환합니다.")
  @GetMapping("/count")
  public int getTotalCount() {
    return userNoticeService.getTotalCount();
  }
}