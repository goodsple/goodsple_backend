package com.goodsple.features.notice.service;

import com.goodsple.features.notice.dto.NoticeListDto;

import java.util.List;

public interface NoticeListService {

  List<NoticeListDto> getNoticeList(String title, Boolean isPopup, int page, int size);
  int getNoticeListTotalCount(String title, Boolean isPopup);

}
