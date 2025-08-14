package com.goodsple.features.notice.service.impl;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.dto.NoticeListFilterDto;
import com.goodsple.features.notice.mapper.NoticeListMapper;
import com.goodsple.features.notice.service.NoticeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeListServiceImpl implements NoticeListService {

  private final NoticeListMapper noticeListMapper;

  @Override
  public List<NoticeListDto> getNoticeList(NoticeListFilterDto filter) {
    return noticeListMapper.selectNoticeList(filter);
  }

  @Override
  public int getNoticeCount(NoticeListFilterDto filter) {
    return noticeListMapper.countNoticeList(filter);
  }

  @Override
  public NoticeListDto getNoticeDetail(Long noticeId) {
    NoticeListDto notice = noticeListMapper.selectNoticeDetail(noticeId);
    // 첨부파일, 팝업 정보도 가져오도록 필요시 매핑
    return notice;
  }
}
