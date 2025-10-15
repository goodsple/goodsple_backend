package com.goodsple.features.admin.chatbot.service;


import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseCreateRequest;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseForwardRequest;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseUpdateRequest;
import com.goodsple.features.admin.chatbot.dto.response.KnowledgeBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminChatbotService {

    private final RestTemplate restTemplate;

    @Value("${chatbot-api.url}")
    private String pythonApiBaseUrl;

    // 1. 목록 조회
    public List<KnowledgeBaseResponse> getKnowledgeList() {
        String url = pythonApiBaseUrl + "/knowledge";
        KnowledgeBaseResponse[] response = restTemplate.getForObject(url, KnowledgeBaseResponse[].class);
        return Arrays.asList(response);
    }

    // 2. 항목 추가
    public KnowledgeBaseResponse createKnowledge(KnowledgeBaseCreateRequest request) {
        String url = pythonApiBaseUrl + "/knowledge";

        // 1. 프론트에서 받은 DTO를, 파이썬 통신 전용 '번역 DTO'로 변환합니다.
        KnowledgeBaseForwardRequest forwardRequest = KnowledgeBaseForwardRequest.from(request);

        // 2. '번역된' DTO를 Python 서버로 전송합니다.
        return restTemplate.postForObject(url, forwardRequest, KnowledgeBaseResponse.class);
    }

    // 3. 항목 수정
    public KnowledgeBaseResponse updateKnowledge(Long id, KnowledgeBaseUpdateRequest request) {
        String url = pythonApiBaseUrl + "/knowledge/" + id;
        HttpEntity<KnowledgeBaseUpdateRequest> requestEntity = new HttpEntity<>(request);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, KnowledgeBaseResponse.class).getBody();
    }

    // 4. 항목 삭제
    public KnowledgeBaseResponse deleteKnowledge(Long id) {
        String url = pythonApiBaseUrl + "/knowledge/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, KnowledgeBaseResponse.class).getBody();
    }
}
