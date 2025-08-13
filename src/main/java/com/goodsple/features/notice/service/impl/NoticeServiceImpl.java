package com.goodsple.features.notice.service.impl;

import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import com.goodsple.features.notice.mapper.NoticeMapper;
import com.goodsple.features.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

  private final NoticeMapper noticeMapper;

  @Override
  @Transactional
  public void createNotice(NoticeDto noticeDto) {

    // 일반 공지사항 등록
    noticeMapper.createNotice(noticeDto);

    // 첨부파일 저장
    if(noticeDto.getAttachments() != null && !noticeDto.getAttachments().isEmpty()) {
      noticeDto.getAttachments().forEach(file -> {
        file.setNoticeId(noticeDto.getNoticeId());
        noticeMapper.insertNoticeAttachment(file);
      });
    }

    // 팝업 등록 여부 확인 후 저장
    if (Boolean.TRUE.equals(noticeDto.getIsPopup()) && noticeDto.getPopupInfo() != null) {
      PopupNoticeDto popupNotice = noticeDto.getPopupInfo();

      if (popupNotice.getPopupStart() == null || popupNotice.getPopupEnd() == null) {
        throw new IllegalArgumentException("팝업 시작일과 종료일은 반드시 입력해야 합니다.");
      }

      if (popupNotice.getPopupImageUrl() == null && popupNotice.getPopupSummary() == null) {
        throw new RuntimeException("팝업 이미지 또는 요약 메세지 중 최소 하나는 입력해야 합니다.");
      }

      popupNotice.setNoticeId(noticeDto.getNoticeId());

      log.info("Popup Notice DTO 저장 전: {}", popupNotice);

      noticeMapper.createPopupNotice(popupNotice);

      log.info("Popup Notice 저장 완료");
    }
  }

}
