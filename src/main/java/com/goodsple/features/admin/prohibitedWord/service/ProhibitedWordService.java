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
        if (mapper.countByWord(request.getWord()) > 0) {
            throw new IllegalArgumentException("이미 등록된 금칙어입니다: " + request.getWord());
        }

        mapper.insertWord(request.getWord());
    }

    // 금칙어 삭제
    @Transactional
    public void deleteWords(List<Long> ids) {
        mapper.deleteWords(ids);
    }

    // 활성/비활성 토글
    @Transactional
    public void toggleWord(Long id) {
        int updated = mapper.toggleWordActive(id);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 금칙어 ID: " + id);
        }
    }

    // 활성화 금칙어 조회
    public List<String> getActiveWords() {
        return mapper.selectActiveWords();
    }
}
