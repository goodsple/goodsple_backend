package com.goodsple.features.search.controller;

import com.goodsple.features.search.dto.SearchPostResponse;
import com.goodsple.features.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/searchPosts")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<SearchPostResponse>> searchExchangePosts(
            @RequestParam("keyword") String keyword) {
        List<SearchPostResponse> results = searchService.searchPosts(keyword);
        return ResponseEntity.ok(results);
    }

}
