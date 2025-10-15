package com.goodsple.features.admin.chatbot.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeBaseUpdateRequest {
    @JsonAlias({"knowledgeIntent","knowledge_intent"})
    private String knowledgeIntent;
    @JsonAlias({"knowledgeQuestion","knowledge_question"})
    private String knowledgeQuestion;
    @JsonAlias({"knowledgeAnswer","knowledge_answer"})
    private String knowledgeAnswer;
    @JsonAlias({"knowledgeIsFaq","knowledge_is_faq"})
    private Boolean knowledgeIsFaq;
    @JsonAlias({"knowledgeIsActive","knowledge_is_active"})
    private Boolean knowledgeIsActive;
}
