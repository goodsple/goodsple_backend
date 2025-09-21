package com.goodsple.features.myexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyExchangePostUpdateDto {
  private String exchangePostTitle;
  private String exchangePostContent;
  private String postTradeType;   // DIRECT, DELIVERY, BOTH
  private String location;        // 직거래 희망 장소 (선택사항)
  private Integer deliveryFee;    // 택배비 (선택사항)
  private String imageUrl;        // 썸네일 이미지 (선택사항)
}
