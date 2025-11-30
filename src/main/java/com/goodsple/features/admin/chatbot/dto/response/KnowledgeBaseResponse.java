package com.goodsple.features.admin.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeBaseResponse {
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