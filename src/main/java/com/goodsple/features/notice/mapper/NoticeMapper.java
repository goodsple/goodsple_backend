package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

  // 일반 공지사항 등록
  void createNotice(NoticeDto noticeDto);
  void insertNoticeAttachment(NoticeAttachmentDto attachmentDto);

  // 팝업 공지사항 등록
  void createPopupNotice(PopupNoticeDto popupNoticeDto);

}
