package com.goodsple.features.noticelist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNoticeDetailDto {

  private Long noticeId;
  private String noticeTitle;
  private String noticeContent;
  private OffsetDateTime noticeCreatedAt;
  private OffsetDateTime noticeUpdatedAt;

}
