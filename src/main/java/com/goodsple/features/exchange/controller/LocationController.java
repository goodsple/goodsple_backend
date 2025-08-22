package com.goodsple.features.exchange.controller;

import com.goodsple.features.exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  @GetMapping("/region")
  public ResponseEntity<Map<String, Object>> getRegion(@RequestParam double latitude,
                                                      @RequestParam double longitude) {

    Map<String, Object> result = locationService.getRegion(latitude, longitude);
    return ResponseEntity.ok(result);
  }

}
