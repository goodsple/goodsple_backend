package com.goodsple.features.admin.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // Python 서버가 보내는 필드 중 여기서 정의하지 않은 것은 무시
public class KnowledgeBaseResponse {
    @JsonProperty("knowledge_id")
    private Long knowledgeId;

    @JsonProperty("knowledge_intent")
    private String knowledgeIntent;

    @JsonProperty("knowledge_question")
    private String knowledgeQuestion;

    @JsonProperty("knowledge_answer")
    private String knowledgeAnswer;

    @JsonProperty("knowledge_is_faq")
    private boolean knowledgeIsFaq;

    @JsonProperty("knowledge_is_active")
    private boolean knowledgeIsActive;

    @JsonProperty("knowledge_created_at")
    private LocalDateTime knowledgeCreatedAt;

    @JsonProperty("knowledge_updated_at")
    private LocalDateTime knowledgeUpdatedAt;
}