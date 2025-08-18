package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.service.NoticeListService;
import com.goodsple.features.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/notices")
@Tag(name = "Notice API", description = "공지사항 CRUD API")
@RequiredArgsConstructor
public class NoticeAdminController {

  private final NoticeService noticeService;
  private final NoticeListService noticeListService;

  @PostMapping
  public ResponseEntity<String> createNotice(@RequestBody NoticeDto noticeDto) {
    noticeService.createNotice(noticeDto);
    return ResponseEntity.ok("공지사항이 등록되었습니다.");
  }

  @GetMapping
  public List<NoticeListDto> getNoticeList(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Boolean isPopup
  ) {
    return noticeListService.getNoticeList(title, isPopup);
  }

}
