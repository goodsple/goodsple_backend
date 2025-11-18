package com.goodsple.features.map.mapper;

import com.goodsple.features.map.dto.response.MapGoodsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MapMapper {
    List<MapGoodsResponse> findGoodsInBounds(
            @Param("swLng") double swLng,
            @Param("swLat") double swLat,
            @Param("neLng") double neLng,
            @Param("neLat") double neLat
    );
}