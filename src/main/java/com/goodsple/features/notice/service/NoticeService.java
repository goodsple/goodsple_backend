package com.goodsple.features.notice.service;

import com.goodsple.features.notice.dto.NoticeDto;


public interface NoticeService {

  Long createNotice(NoticeDto dto);
  void updateNotice(NoticeDto dto);
  void deleteNotice(Long noticeId);

}
