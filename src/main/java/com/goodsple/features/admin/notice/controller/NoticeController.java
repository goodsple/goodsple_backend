package com.goodsple.features.admin.notice.controller;

import com.goodsple.features.admin.notice.dto.NoticeDto;
import com.goodsple.features.admin.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/notices")
@Tag(name = "Notice API", description = "공지사항 CRUD API")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  // 등록
  @PostMapping
  public ResponseEntity<Long> createNotice(@RequestBody NoticeDto dto) {
    Long noticeId = noticeService.createNotice(dto);
    return ResponseEntity.ok(noticeId);
  }

  // 수정을 위해 조회
  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDto> getNotice(@PathVariable Long noticeId) {
    NoticeDto dto = noticeService.getNotice(noticeId);
    return ResponseEntity.ok(dto);
  }

  // 수정
  @PutMapping("/{noticeId}")
  public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId,
                                           @RequestBody NoticeDto dto) {
    dto.setNoticeId(noticeId);
    noticeService.updateNotice(dto);
    return ResponseEntity.ok().build();
  }

  // 삭제
  @DeleteMapping("/{noticeId}")
  public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
    noticeService.deleteNotice(noticeId);
    return ResponseEntity.ok().build();
  }
}