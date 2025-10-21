package com.goodsple.features.chatbot.service;


import com.goodsple.features.chatbot.dto.request.ChatbotRequest;
import com.goodsple.features.chatbot.dto.response.ChatbotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final RestTemplate restTemplate;

    @Value("${chatbot-api.url}") // ex) http://127.0.0.1:8000/api
    private String pythonBase;

    public ChatbotResponse handle(ChatbotRequest req) {
        try {
            String url = pythonBase + "/answer";
            ChatbotResponse res = restTemplate.postForObject(
                    url, Map.of("text", req.getText()), ChatbotResponse.class);
            return res != null ? res : fallback();
        } catch (Exception e) {
            return fallback();
        }
    }

    private ChatbotResponse fallback() {
        return new ChatbotResponse("일시적인 오류입니다. 잠시 후 다시 시도해주세요.", "fallback", 0.0);
    }
}
