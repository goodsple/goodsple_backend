package com.goodsple.features.admin.chatbot.dto.request;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeBaseCreateRequest {
    @JsonAlias({"knowledgeIntent","knowledge_intent"})
    private String knowledgeIntent;

    @JsonAlias({"knowledgeQuestion","knowledge_question"})
    private String knowledgeQuestion;

    @JsonAlias({"knowledgeAnswer","knowledge_answer"})
    private String knowledgeAnswer;
}
