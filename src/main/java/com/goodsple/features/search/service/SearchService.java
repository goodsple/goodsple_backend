package com.goodsple.features.search.service;

import com.goodsple.features.search.dto.SearchPostResponse;
import com.goodsple.features.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;

    public List<SearchPostResponse> searchPosts(String keyword) {
        return searchMapper.searchPosts(keyword);
    }
}
