package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class PopupNoticeController {

  private final NoticeService noticeService;

  @GetMapping("/popup")
  public ResponseEntity<List<NoticeDto>> getActivePopups() {
    List<NoticeDto> popups = noticeService.getActivePopups();
    return ResponseEntity.ok(popups);
  }

  // 특정 공지 ID의 팝업 정보 가져오기
  @GetMapping("/{noticeId}/popup")
  public ResponseEntity<NoticeDto> getPopupByNoticeId(@PathVariable Long noticeId) {
    NoticeDto notice = noticeService.getNotice(noticeId);
    if (notice != null && notice.getPopupInfo() != null) {
      return ResponseEntity.ok(notice);
    } else {
      return ResponseEntity.noContent().build(); // DB에 팝업 정보 없으면 204
    }
  }

  // 공지사항 상세보기 (일반 조회)
  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long noticeId) {
    NoticeDto notice = noticeService.getNotice(noticeId);
    if (notice != null) {
      return ResponseEntity.ok(notice);
    } else {
      return ResponseEntity.notFound().build();
    }
  }


}
