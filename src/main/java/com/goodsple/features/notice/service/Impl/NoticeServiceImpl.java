package com.goodsple.features.notice.service.Impl;


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
  public Long createNotice(NoticeDto dto) {
    noticeMapper.insertNotice(dto);
    if (dto.getAttachments() != null) {
      dto.getAttachments().forEach(att -> {
        att.setNoticeId(dto.getNoticeId());
        noticeMapper.insertAttachment(att);
      });
    }
    if (dto.getPopupInfo() != null) {
      dto.getPopupInfo().setNoticeId(dto.getNoticeId());
      noticeMapper.insertPopup(dto.getPopupInfo());
    }
    return dto.getNoticeId();
  }

  @Override
  @Transactional
  public void updateNotice(NoticeDto dto) {
    noticeMapper.updateNotice(dto);
    // 첨부파일이나 팝업 업데이트 로직 필요 시 구현
  }

  @Override
  @Transactional
  public void deleteNotice(Long noticeId) {
    noticeMapper.deleteNotice(noticeId);
  }
}