package com.goodsple.features.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListDto {

  private Long noticeId;
  private String noticeTitle;
  private String noticeContent;
  private OffsetDateTime noticeCreatedAt;
  private Boolean isPopup;

  private List<NoticeAttachmentDto> attachments; // 첨부파일
  private PopupNoticeDto popupInfo;             // 팝업 정보

}
