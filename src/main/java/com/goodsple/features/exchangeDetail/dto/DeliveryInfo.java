package com.goodsple.features.exchangeDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {

  private int normal;
  private int half;
  private String halfDeliveryType;

}
