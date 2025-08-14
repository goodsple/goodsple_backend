package com.goodsple.features.notice.service;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.dto.NoticeListFilterDto;

import java.util.List;

public interface NoticeListService {

  List<NoticeListDto> getNoticeList(NoticeListFilterDto filter);

  int getNoticeCount(NoticeListFilterDto filter);

  NoticeListDto getNoticeDetail(Long noticeId);

}
