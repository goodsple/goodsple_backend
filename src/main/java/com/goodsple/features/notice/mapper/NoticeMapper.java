package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeAttachmentDto;
import com.goodsple.features.notice.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

  void createNotice(NoticeDto noticeDto);
  void insertNoticeAttachment(NoticeAttachmentDto attachmentDto);

}
