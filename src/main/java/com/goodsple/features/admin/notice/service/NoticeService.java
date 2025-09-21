package com.goodsple.features.admin.notice.service;

import com.goodsple.features.admin.notice.dto.NoticeDto;

import java.util.List;


public interface NoticeService {

  Long createNotice(NoticeDto dto);
  NoticeDto getNotice(Long noticeId);
  void updateNotice(NoticeDto dto);
  void deleteNotice(Long noticeId);

  // 팝업 공지사항
  List<NoticeDto> getActivePopups();

}
