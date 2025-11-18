package com.goodsple.features.myexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
  public class MyExchangePostDto {

    private Long exchangePostId;          // exchange_post_id
    private String exchangePostTitle;     // exchange_post_title
    private String postTradeStatus;       // post_trade_status
    private String postTradeType;         // post_trade_type
    private String updatedAt;             // exchange_post_updated_at (TO_CHAR로 문자열 변환)
    private String postLocationName;      // post_location_name
    private Integer deliveryPriceNormal;  // delivery_price_normal
    private Integer deliveryPriceHalf;    // delivery_price_half
    private String imageUrl;

}
