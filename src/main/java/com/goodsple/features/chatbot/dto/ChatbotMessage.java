package com.goodsple.features.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotMessage {
    private Long messageId;
    private Long logId;
    private String messageSender; // 'user' or 'bot'
    private String messageText;
    private Integer messageOrderIndex;
    private OffsetDateTime messageTimestamp;
}