package com.goodsple.features.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

  private Long noticeId;
  private Long userId;  // fk
  private String noticeTitle;
  private String noticeContent;
  private OffsetDateTime noticeCreatedAt;
  private OffsetDateTime noticeUpdatedAt;
  private Boolean isPopup;
  private List<NoticeAttachmentDto> attachments;

  private PopupNoticeDto popupInfo;
}
