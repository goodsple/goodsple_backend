package com.goodsple.features.notice.controller;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.dto.NoticeListFilterDto;
import com.goodsple.features.notice.service.NoticeListService;
import com.goodsple.features.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class NoticeAdminListController {

  private final NoticeListService noticeListService;

  @GetMapping
  public List<NoticeListDto> getNoticeList(NoticeListFilterDto filter) {
    return noticeListService.getNoticeList(filter);
  }

  @GetMapping("/{noticeId}")
  public NoticeListDto getNoticeDetail(@PathVariable Long noticeId) {
    return noticeListService.getNoticeDetail(noticeId);
  }

}
