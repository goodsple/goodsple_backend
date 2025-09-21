package com.goodsple.features.admin.notice.service.Impl;


import com.goodsple.features.admin.notice.dto.NoticeDto;
import com.goodsple.features.admin.notice.mapper.NoticeMapper;
import com.goodsple.features.admin.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
  @Transactional(readOnly = true)
  public NoticeDto getNotice(Long noticeId) {
    return noticeMapper.selectNoticeById(noticeId);
  }

  @Override
  @Transactional
  public void updateNotice(NoticeDto dto) {
    noticeMapper.updateNotice(dto);

    if (dto.getPopupInfo() != null) {
      dto.getPopupInfo().setNoticeId(dto.getNoticeId());

      if (dto.getPopupInfo().getPopupId() != null) {
        // 기존 팝업 정보가 있으면 update
        noticeMapper.updatePopup(dto.getPopupInfo());
      } else {
        // 기존 팝업이 없었으면 새로 insert
        noticeMapper.insertPopup(dto.getPopupInfo());
      }
    }
    // 체크박스 해제 시 isPopup만 false로 업데이트되고, 기존 popupInfo는 그대로 유지
  }

  @Override
  @Transactional
  public void deleteNotice(Long noticeId) {
    noticeMapper.deleteNotice(noticeId);
  }

  @Transactional(readOnly = true)
  public List<NoticeDto> getActivePopups() {
    return noticeMapper.selectActivePopupNotices();
  }

}