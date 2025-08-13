package com.goodsple.features.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupNoticeDto {

  private Long popupId;
  private Long noticeId;  // fk
  private LocalDate popupStart;
  private LocalDate popupEnd;
  private String popupImageUrl;
  private String popupSummary;

}
