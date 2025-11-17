package com.goodsple.features.admin.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter // Setter 추가
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Python 서버가 보내는 필드 중 여기서 정의하지 않은 것은 무시
public class KnowledgeBaseResponse {
    // '보낼 때'는 snake_case, '받을 때'는 둘 다 허용
    @JsonProperty("knowledge_id")
    @JsonAlias({"knowledgeId", "knowledge_id"})
    private Long knowledgeId;

    @JsonProperty("knowledge_intent")
    @JsonAlias({"knowledgeIntent", "knowledge_intent"})
    private String knowledgeIntent;

    @JsonProperty("knowledge_question")
    @JsonAlias({"knowledgeQuestion", "knowledge_question"})
    private String knowledgeQuestion;

    @JsonProperty("knowledge_answer")
    @JsonAlias({"knowledgeAnswer", "knowledge_answer"})
    private String knowledgeAnswer;

    @JsonProperty("knowledge_is_faq")
    @JsonAlias({"knowledgeIsFaq", "knowledge_is_faq"})
    private boolean knowledgeIsFaq;

    @JsonProperty("knowledge_is_active")
    @JsonAlias({"knowledgeIsActive", "knowledge_is_active"})
    private boolean knowledgeIsActive;

    @JsonProperty("knowledge_created_at")
    @JsonAlias({"knowledgeCreatedAt", "knowledge_created_at"})
    private OffsetDateTime knowledgeCreatedAt;

    @JsonProperty("knowledge_updated_at")
    @JsonAlias({"knowledgeUpdatedAt", "knowledge_updated_at"})
    private OffsetDateTime knowledgeUpdatedAt;
}