package com.goodsple.features.notice.dto;

import lombok.Data;

@Data
public class NoticeListFilterDto {
  private String keyword;    // 제목/내용 검색
  private int page;          // 페이지 번호
  private int size;          // 페이지당 개수
  private int offset; // = page * size
}