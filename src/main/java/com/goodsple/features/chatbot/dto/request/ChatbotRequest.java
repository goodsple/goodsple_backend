package com.goodsple.features.chatbot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatbotRequest {
    private String sessionId;
    private String text;
    private String source;

    private Boolean isNewChat;
    private Long userId;
    private Long logId;
}
