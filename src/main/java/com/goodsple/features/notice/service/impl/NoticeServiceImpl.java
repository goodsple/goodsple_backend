package com.goodsple.features.notice.service.impl;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.mapper.NoticeMapper;
import com.goodsple.features.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

  private final NoticeMapper noticeMapper;

  @Override
  @Transactional
  public void createNotice(NoticeDto noticeDto) {
    noticeMapper.createNotice(noticeDto);

    if(noticeDto.getAttachments() != null && !noticeDto.getAttachments().isEmpty()) {
      noticeDto.getAttachments().forEach(file -> {
        file.setNoticeId(noticeDto.getNoticeId());
        noticeMapper.insertNoticeAttachment(file);
      });
    }
  }

}
