package com.goodsple.features.admin.notice.service.Impl;

import com.goodsple.features.admin.notice.dto.NoticeListDto;
import com.goodsple.features.admin.notice.mapper.NoticeListMapper;
import com.goodsple.features.admin.notice.service.NoticeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeListServiceImpl implements NoticeListService {

  private final NoticeListMapper noticeListMapper;

  @Override
  public List<NoticeListDto> getNoticeList(String title, Boolean isPopup, int page, int size) {
    // page가 1보다 작으면 1로 설정하여 offset이 음수가 되지 않도록 방지
    if (page < 1) {
      page = 1;
    }

    int offset = (page - 1) * size;
    return noticeListMapper.selectNoticeList(title, isPopup, offset, size);
  }

  @Override
  public int getNoticeListTotalCount(String title, Boolean isPopup) {
    return noticeListMapper.selectNoticeListCount(title, isPopup);
  }

}
