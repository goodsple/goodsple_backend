package com.goodsple.features.postlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
  private Long exchangePostId;
  private String writer;
  private String writerNickname;
  private OffsetDateTime exchangePostCreatedAt;
  private String postTradeStatus; // 거래상태
  private String exchangePostTitle;
}
