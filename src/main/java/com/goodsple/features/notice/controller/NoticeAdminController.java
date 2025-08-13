package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice API", description = "공지사항 CRUD API")
public class NoticeAdminController {

  private final NoticeService noticeService;

  @PostMapping
  @Operation(summary = "공지사항 등록", description = "공지사항과 첨부파일, 팝업을 등록합니다."
  )
  public void createNotice(@RequestBody NoticeDto noticeDto) {
    log.info("createNotice 호출됨, noticeDto: {}", noticeDto);
    noticeService.createNotice(noticeDto);
  }

}
