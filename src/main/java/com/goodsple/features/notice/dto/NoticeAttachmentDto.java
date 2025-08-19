package com.goodsple.features.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttachmentDto {

  private Long attachmentId;
  private Long noticeId;  // fk
  private String noticeFileUrl;
  private String noticeFileType;
  private Integer noticeSortOrder;

}