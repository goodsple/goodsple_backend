package com.goodsple.features.map.dto.response;

import lombok.Data;

@Data
public class MapGoodsResponse {
    private Long id;
    private String name;
    private String tradeType;
    private double lat;
    private double lng;
    private String imageUrl;
}