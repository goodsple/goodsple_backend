package com.goodsple.features.admin.chatbot.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
public class KnowledgeBaseForwardRequest {

    private String knowledgeIntent;
    private String knowledgeQuestion;
    private String knowledgeAnswer;

    public static KnowledgeBaseForwardRequest from(KnowledgeBaseCreateRequest originalRequest) {
        return new KnowledgeBaseForwardRequest(
                originalRequest.getKnowledgeIntent(),
                originalRequest.getKnowledgeQuestion(),
                originalRequest.getKnowledgeAnswer()
        );
    }
}