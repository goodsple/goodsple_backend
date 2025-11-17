package com.goodsple.features.popular.controller;

import com.goodsple.features.popular.dto.PopularKeywordDTO;
import com.goodsple.features.popular.service.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/popular")
@RequiredArgsConstructor
public class PopularSearchController {

  private final PopularSearchService service;

  @GetMapping("/top")
  public List<PopularKeywordDTO> getTopKeywords(@RequestParam(defaultValue = "10") int limit) {
    return service.getTopKeywords(limit);
  }

  @PostMapping("/record")
  public void recordSearch(@RequestParam String keyword) {
    service.recordSearch(keyword);
  }

}
