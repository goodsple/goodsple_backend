package com.goodsple.features.chatbot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatbotRequest {
    private String sessionId;   // 옵션
    private String text;        // 필수
    private String source;      // "FAQ"|"QNA" 옵션
}
