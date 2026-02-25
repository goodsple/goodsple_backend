package com.goodsple.features.exchangedetail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostWriterDto {

  private Long id;                // user_id
  private String nickname;        // 닉네임
  private String profileImageUrl; // 프로필 이미지
  private Integer badgeLevel;          // 레벨
  private String badgeImageUrl;   // 뱃지 이미지
  private String badgeName;       // 뱃지 이름

}
