package com.goodsple.features.exchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final RestTemplate restTemplate = new RestTemplate();

  private final String REST_API_KEY = "b26e6bfdf513ea8d31a56ab317271e20"; // ← 여기에 REST API 키 넣기

  // 좌표 → 행정동 코드
  public Map<String, Object> getRegion(double latitude, double longitude) {
    String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
        + "?x=" + longitude + "&y=" + latitude;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + REST_API_KEY);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    return response.getBody();
  }

  // 주소 → 좌표 변환
  public Map<String, Object> getCoord(String address) {
    String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + REST_API_KEY);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

    // documents[0] 안에 x=longitude, y=latitude 있음
    List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");
    if (documents == null || documents.isEmpty()) {
      throw new RuntimeException("주소 변환 실패");
    }

    Map<String, Object> doc = documents.get(0);
    Map<String, Object> result = new HashMap<>();
    result.put("latitude", doc.get("y"));
    result.put("longitude", doc.get("x"));

    return result;
  }
}
