package com.goodsple.features.admin.popularKeyword.controller;

import com.goodsple.features.admin.popularKeyword.dto.PopularKeywordDTO;
import com.goodsple.features.admin.popularKeyword.service.PopularKeywordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/popular-keywords")
public class PopularKeywordController {

  private final PopularKeywordService service;

  public PopularKeywordController(PopularKeywordService service) {
    this.service = service;
  }

  // 전체 조회
  @GetMapping
  public ResponseEntity<List<PopularKeywordDTO>> getAllKeywords() {
    return ResponseEntity.ok(service.getAllKeywords());
  }

  // 차단
  @PutMapping("/{keywordId}/block")
  public ResponseEntity<Void> blockKeyword(@PathVariable Long keywordId) {
    service.updateStatus(keywordId, "BLOCKED");
    return ResponseEntity.ok().build();
  }

  // 차단 해제
  @PutMapping("/{keywordId}/visible")
  public ResponseEntity<Void> makeVisible(@PathVariable Long keywordId) {
    service.updateStatus(keywordId, "VISIBLE");
    return ResponseEntity.ok().build();
  }

}
