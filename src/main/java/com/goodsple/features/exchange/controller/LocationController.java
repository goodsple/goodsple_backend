package com.goodsple.features.exchange.controller;

import com.goodsple.features.exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  // 좌표 → 행정동 코드 변환 (내 위치 기능)
  @GetMapping("/region")
  public ResponseEntity<Map<String, Object>> getRegion(@RequestParam double latitude,
                                                      @RequestParam double longitude) {

    Map<String, Object> result = locationService.getRegion(latitude, longitude);
    return ResponseEntity.ok(result);
  }

  // 소 → 좌표 변환 (주소 검색 기능)
  @GetMapping("/coord")
  public ResponseEntity<Map<String, Object>> getCoord(@RequestParam String address) {
    Map<String, Object> result = locationService.getCoord(address);
    return ResponseEntity.ok(result);
  }

}
