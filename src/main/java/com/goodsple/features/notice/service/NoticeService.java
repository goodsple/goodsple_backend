package com.goodsple.features.notice.service;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import com.goodsple.features.notice.mapper.NoticeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;


  @Service
  public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
      this.noticeMapper = noticeMapper;
    }

    public void createNotice(NoticeDto noticeDto) {
      // 1️. 팝업 검증
      if (Boolean.TRUE.equals(noticeDto.getIsPopup())) {
        PopupNoticeDto popup = noticeDto.getPopupInfo();
        if (popup == null) {
          throw new IllegalArgumentException("팝업 정보가 없습니다.");
        }
        LocalDate start = popup.getPopupStart();
        LocalDate end = popup.getPopupEnd();
        if (start == null || end == null) {
          throw new IllegalArgumentException("팝업 시작일과 종료일을 반드시 선택해야 합니다.");
        }
        if (!StringUtils.hasText(popup.getPopupImageUrl()) && !StringUtils.hasText(popup.getPopupSummary())) {
          throw new IllegalArgumentException("팝업 이미지 또는 요약 메시지 중 최소 하나는 입력해야 합니다.");
        }
      } else {
        noticeDto.setIsPopup(false);
        noticeDto.setPopupInfo(null);
      }

      // 2️. 공지사항 등록
      noticeMapper.insertNotice(noticeDto);
      Long noticeId = noticeDto.getNoticeId();

      // 3️. 첨부파일 등록
      if (!CollectionUtils.isEmpty(noticeDto.getAttachments())) {
        for (NoticeAttachmentDto attach : noticeDto.getAttachments()) {
          attach.setNoticeId(noticeId);
          noticeMapper.insertAttachment(attach);
        }
      }

      // 4️. 팝업 정보 등록
      if (Boolean.TRUE.equals(noticeDto.getIsPopup()) && noticeDto.getPopupInfo() != null) {
        PopupNoticeDto popup = noticeDto.getPopupInfo();
        popup.setNoticeId(noticeId);
        noticeMapper.insertPopup(popup);
      }
    }
  }

