package com.goodsple.features.exchangeDetail.dto;

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
  private Integer level;          // 레벨
  private String badgeImageUrl;   // 뱃지 이미지

}
