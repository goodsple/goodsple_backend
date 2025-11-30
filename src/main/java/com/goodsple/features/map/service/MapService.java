package com.goodsple.features.map.service;

import com.goodsple.features.map.dto.response.MapGoodsResponse;
import com.goodsple.features.map.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapMapper mapMapper;

    @Transactional(readOnly = true)
    public List<MapGoodsResponse> getGoodsInBounds(double swLat, double swLng, double neLat, double neLng) {
        return mapMapper.findGoodsInBounds(swLng, swLat, neLng, neLat);
    }
}