package com.goodsple.features.admin.chatbot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class KnowledgeBaseUpdateForwardRequest {

    private String knowledgeIntent;
    private String knowledgeQuestion;
    private String knowledgeAnswer;
    private Boolean knowledgeIsFaq;
    private Boolean knowledgeIsActive;

    public static KnowledgeBaseUpdateForwardRequest from(KnowledgeBaseUpdateRequest originalRequest) {
        return new KnowledgeBaseUpdateForwardRequest(
                originalRequest.getKnowledgeIntent(),
                originalRequest.getKnowledgeQuestion(),
                originalRequest.getKnowledgeAnswer(),
                originalRequest.getKnowledgeIsFaq(),
                originalRequest.getKnowledgeIsActive()
        );
    }
}