package com.goodsple.features.exchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final RestTemplate restTemplate = new RestTemplate();

  public Map<String, Object> getRegion(double latitude, double longitude) {
    String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
        + "?x=" + longitude + "&y=" + latitude;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + "b26e6bfdf513ea8d31a56ab317271e20");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    return response.getBody();
  }
}
