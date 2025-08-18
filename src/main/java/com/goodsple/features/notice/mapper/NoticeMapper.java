package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import com.goodsple.features.notice.dto.PopupNoticeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

  int insertNotice(NoticeDto notice);

  int insertAttachment(NoticeAttachmentDto attachment);

  int insertPopup(PopupNoticeDto popup);

}
