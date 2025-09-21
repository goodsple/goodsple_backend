package com.goodsple.features.noticelist.service;

import com.goodsple.features.noticelist.dto.UserNoticeDto;

import java.util.List;

public interface UserNoticeService {
  List<UserNoticeDto> getNotices(int page, int size);
  int getTotalCount();
}
