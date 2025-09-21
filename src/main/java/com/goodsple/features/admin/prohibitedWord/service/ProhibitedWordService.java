package com.goodsple.features.admin.prohibitedWord.service;

import com.goodsple.features.admin.prohibitedWord.dto.ProhibitedWordDTO;
import com.goodsple.features.admin.prohibitedWord.dto.ProhibitedWordRequest;
import com.goodsple.features.admin.prohibitedWord.mapper.ProhibitedWordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProhibitedWordService {

    private final ProhibitedWordMapper mapper;

    public ProhibitedWordService(ProhibitedWordMapper mapper) {
        this.mapper = mapper;
    }

    // 전체 조회
    public List<ProhibitedWordDTO> getAllWords() {
        return mapper.selectAllWords();
    }

    // 금칙어 추가
    @Transactional
    public void addWord(ProhibitedWordRequest request) {
        mapper.insertWord(request.getWord());
    }

    // 금칙어 삭제
    @Transactional
    public void deleteWords(List<Long> ids) {
        mapper.deleteWords(ids);
    }
}
