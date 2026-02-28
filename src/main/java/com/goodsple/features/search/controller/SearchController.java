package com.goodsple.features.search.controller;

import com.goodsple.features.popular.dto.PopularKeywordDTO;
import com.goodsple.features.popular.service.PopularSearchService;
import com.goodsple.features.search.dto.SearchPostResponse;
import com.goodsple.features.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/searchPosts")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final PopularSearchService popularSearchService;


//    @GetMapping("/search")
//    public ResponseEntity<?> searchExchangePosts(
//            @RequestParam("keyword") String keyword) {
//
//        searchService.validateKeyword(keyword);
//
//
//        // 1. 키워드 검색 시 인기검색어 기록(db저장)
//        popularSearchService.recordSearch(keyword);
//
//        List<SearchPostResponse> results = searchService.searchPosts(keyword);
//        return ResponseEntity.ok(results);
//    }

    @GetMapping("/search")
    public ResponseEntity<?> searchExchangePosts(
            @RequestParam("keyword") String keyword) {

        try {
            searchService.validateKeyword(keyword);

            popularSearchService.recordSearch(keyword);
            List<SearchPostResponse> results =
                    searchService.searchPosts(keyword);

            return ResponseEntity.ok(results);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "금칙어가 포함되어 있습니다."));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularKeywordDTO>> getPopularKeywords(
        @RequestParam(defaultValue = "10") int limit) {

        List<PopularKeywordDTO> popular = popularSearchService.getTopKeywords(limit);
        return ResponseEntity.ok(popular);
    }

}
