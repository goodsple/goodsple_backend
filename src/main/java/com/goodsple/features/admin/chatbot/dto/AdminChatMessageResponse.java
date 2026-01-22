package com.goodsple.features.admin.chatbot.dto;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class AdminChatMessageResponse {
    private String sender;
    private String text;
    private OffsetDateTime timestamp;
}
