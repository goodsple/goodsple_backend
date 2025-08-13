package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

  void createNotice(NoticeDto noticeDto);  // 일반 공지사항 등록
  void insertNoticeAttachment(NoticeAttachmentDto attachmentDto);

  void createPopupNotice(PopupNoticeDto popupNoticeDto);  // 팝업 공지사항 등록

}
