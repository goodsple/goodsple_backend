// src/main/java/com/goodsple/features/admin/chatbot/dto/request/KnowledgeBaseForwardRequest.java

package com.goodsple.features.admin.chatbot.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 이 어노테이션이 마법을 부립니다!
// 이 클래스의 모든 camelCase 필드를 JSON으로 바꿀 때 snake_case로 자동 변환해줍니다.
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor // 필드를 초기화하는 생성자를 자동으로 만듭니다.
public class KnowledgeBaseForwardRequest {

    private String knowledgeIntent;
    private String knowledgeQuestion;
    private String knowledgeAnswer;

    // 생성자: 기존 DTO를 받아서 이 DTO를 쉽게 만들 수 있도록 합니다.
    public static KnowledgeBaseForwardRequest from(KnowledgeBaseCreateRequest originalRequest) {
        return new KnowledgeBaseForwardRequest(
                originalRequest.getKnowledgeIntent(),
                originalRequest.getKnowledgeQuestion(),
                originalRequest.getKnowledgeAnswer()
        );
    }
}