package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.service.NoticeService;
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

  @PostMapping
  public ResponseEntity<Long> createNotice(@RequestBody NoticeDto dto) {
    Long noticeId = noticeService.createNotice(dto);
    return ResponseEntity.ok(noticeId);
  }

  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDto> getNotice(@PathVariable Long noticeId) {
    NoticeDto dto = noticeService.getNotice(noticeId);
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/{noticeId}")
  public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId,
                                           @RequestBody NoticeDto dto) {
    dto.setNoticeId(noticeId);
    noticeService.updateNotice(dto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{noticeId}")
  public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
    noticeService.deleteNotice(noticeId);
    return ResponseEntity.ok().build();
  }
}