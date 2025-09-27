package com.goodsple.features.map.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MapGoodsResponse {
    private Long id;
    private String name;
    private String tradeType; // [수정] price -> tradeType (String 타입)
    private double lat;
    private double lng;
    private String imageUrl;
}