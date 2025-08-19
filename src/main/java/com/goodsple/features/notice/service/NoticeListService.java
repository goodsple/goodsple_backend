package com.goodsple.features.notice.service;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.mapper.NoticeListMapper;
import com.goodsple.features.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeListService {

  private final NoticeListMapper noticeListMapper;


  public List<NoticeListDto> getNoticeList(String title, Boolean isPopup) {
    return noticeListMapper.selectNoticeList(title, isPopup);
  }

}
