package com.goodsple.features.noticelist.controller;

import com.goodsple.features.noticelist.dto.UserNoticeDetailDto;
import com.goodsple.features.noticelist.service.UserNoticeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/notices")
@RequiredArgsConstructor
@Tag(name = "UserNoticeDetail", description = "공지사항 상세 조회 API")
public class UserNoticeDetailController {

  private final UserNoticeDetailService userNoticeDetailService;

  @Operation(summary = "공지사항 상세 조회", description = "공지사항 ID로 상세 정보를 조회합니다.")
  @GetMapping("/{noticeId}")
  public UserNoticeDetailDto getNotice(@PathVariable Long noticeId) {
    return userNoticeDetailService.getNotice(noticeId);
  }

}
