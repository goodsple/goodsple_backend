package com.goodsple.features.map.controller;

import com.goodsple.features.map.dto.response.MapGoodsResponse;
import com.goodsple.features.map.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "07. 지도 (Map)", description = "지도 기반 굿즈 조회 API")
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @Operation(summary = "영역 내 굿즈 조회", description = "지도 화면의 사각 영역 내에 있는 굿즈 목록을 조회합니다.")
    @GetMapping("/goods")
    public ResponseEntity<List<MapGoodsResponse>> getGoodsInBounds(
            @Parameter(description = "남서쪽 위도 (South-West Latitude)", required = true) @RequestParam double swLat,
            @Parameter(description = "남서쪽 경도 (South-West Longitude)", required = true) @RequestParam double swLng,
            @Parameter(description = "북동쪽 위도 (North-East Latitude)", required = true) @RequestParam double neLat,
            @Parameter(description = "북동쪽 경도 (North-East Longitude)", required = true) @RequestParam double neLng
    ) {
        List<MapGoodsResponse> goods = mapService.getGoodsInBounds(swLat, swLng, neLat, neLng);
        return ResponseEntity.ok(goods);
    }
}