package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.service.NoticeListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notices")
@Tag(name = "Notice API", description = "공지사항 목록 조회 및 페이징 API")
@RequiredArgsConstructor
public class NoticeListController {

  private final NoticeListService noticeListService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getNoticeList(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Boolean isPopup,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {

    List<NoticeListDto> list = noticeListService.getNoticeList(title, isPopup, page, size);
    int totalCount = noticeListService.getNoticeListTotalCount(title, isPopup);

    Map<String, Object> response = new HashMap<>();
    response.put("data", list);
    response.put("page", page);
    response.put("size", size);
    response.put("totalCount", totalCount);

    return ResponseEntity.ok(response);
  }

}
