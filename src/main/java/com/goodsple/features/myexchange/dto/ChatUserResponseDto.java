package com.goodsple.features.myexchange.dto;

import lombok.Data;

@Data
public class ChatUserResponseDto {

  private Long userId;
  private String nickname;
  private String profileImageUrl;
//  private String badgeIcon;
  private Integer lastChatDaysAgo; // 며칠 전

}
