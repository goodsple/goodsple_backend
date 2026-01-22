package com.goodsple.features.admin.chatbot.dto;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class AdminChatLogResponse {
    private Long id;
    private Long userId;
    private String question;
    private String predictedIntent;
    private Double confidence;
    private OffsetDateTime timestamp;
    private String type;
}
