package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

  void insertNotice(NoticeDto notice);

  void updateNotice(NoticeDto notice);

  void deleteNotice(Long noticeId);

  void insertAttachment(NoticeAttachmentDto attachment);

  void insertPopup(PopupNoticeDto popup);

}
