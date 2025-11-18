package com.goodsple.features.admin.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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