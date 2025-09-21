package com.goodsple.features.admin.notice.mapper;

import com.goodsple.features.admin.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.admin.notice.dto.NoticeDto;
import com.goodsple.features.admin.notice.dto.PopupNoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

  void insertNotice(NoticeDto notice);
  NoticeDto selectNoticeById(Long noticeId);
  void updateNotice(NoticeDto notice);
  void deleteNotice(Long noticeId);

  void insertAttachment(NoticeAttachmentDto attachment);

  void insertPopup(PopupNoticeDto popup);
  void updatePopup(PopupNoticeDto popup);

  List<NoticeDto> selectActivePopupNotices();

}
