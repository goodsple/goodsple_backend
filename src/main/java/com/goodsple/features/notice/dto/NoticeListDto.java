package com.goodsple.features.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListDto {

  private Long noticeId;
  private String noticeTitle;
  private OffsetDateTime noticeCreatedAt;
  private OffsetDateTime noticeUpdatedAt;
  private Integer attachmentCount;
  private String userName;
  private Boolean isPopup;
  private LocalDate popupStart;
  private LocalDate popupEnd;

}
