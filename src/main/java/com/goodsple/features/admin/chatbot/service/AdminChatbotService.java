package com.goodsple.features.admin.chatbot.service;


import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseCreateRequest;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseForwardRequest;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseUpdateForwardRequest;
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

    public List<KnowledgeBaseResponse> getKnowledgeList() {
        String url = pythonApiBaseUrl + "/knowledge";
        KnowledgeBaseResponse[] response = restTemplate.getForObject(url, KnowledgeBaseResponse[].class);
        return Arrays.asList(response);
    }

    public KnowledgeBaseResponse createKnowledge(KnowledgeBaseCreateRequest request) {
        String url = pythonApiBaseUrl + "/knowledge";

        KnowledgeBaseForwardRequest forwardRequest = KnowledgeBaseForwardRequest.from(request);

        return restTemplate.postForObject(url, forwardRequest, KnowledgeBaseResponse.class);
    }

    public KnowledgeBaseResponse updateKnowledge(Long id, KnowledgeBaseUpdateRequest request) {
        String url = pythonApiBaseUrl + "/knowledge/" + id;

        KnowledgeBaseUpdateForwardRequest forwardRequest = KnowledgeBaseUpdateForwardRequest.from(request);

        HttpEntity<KnowledgeBaseUpdateForwardRequest> requestEntity = new HttpEntity<>(forwardRequest);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, KnowledgeBaseResponse.class).getBody();
    }

    public KnowledgeBaseResponse deleteKnowledge(Long id) {
        String url = pythonApiBaseUrl + "/knowledge/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, KnowledgeBaseResponse.class).getBody();
    }
}
