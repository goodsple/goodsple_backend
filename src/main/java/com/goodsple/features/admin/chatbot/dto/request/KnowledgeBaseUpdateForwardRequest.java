// src/main/java/com/goodsple/features/admin/chatbot/dto/request/KnowledgeBaseUpdateForwardRequest.java

package com.goodsple.features.admin.chatbot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 이 어노테이션이 camelCase -> snake_case 자동 변환을 해줍니다.
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// 이 어노테이션은 null이 아닌 필드만 JSON에 포함시킵니다 (부분 업데이트를 위해 중요).
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class KnowledgeBaseUpdateForwardRequest {

    private String knowledgeIntent;
    private String knowledgeQuestion;
    private String knowledgeAnswer;
    private Boolean knowledgeIsFaq;
    private Boolean knowledgeIsActive;

    // 생성자: 기존 Update DTO를 받아서 이 DTO를 쉽게 만들 수 있도록 합니다.
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