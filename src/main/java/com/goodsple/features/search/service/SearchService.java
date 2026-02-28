package com.goodsple.features.search.service;

import com.goodsple.features.admin.prohibitedWord.service.ProhibitedWordService;
import com.goodsple.features.search.dto.SearchPostResponse;
import com.goodsple.features.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;
    private final ProhibitedWordService prohibitedWordService;

    public List<SearchPostResponse> searchPosts(String keyword) {

        return searchMapper.searchPosts(keyword);
    }

    public void validateKeyword(String keyword) {
        prohibitedWordService.validateContent(keyword);
    }
}
