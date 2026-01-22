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
public class ChatbotLog {
    private Long logId;
    private String logSessionId;
    private Long userId;
    private String logType; // FAQ / QNA
    private String logInitialQuestion;
    private String logPredictedIntent;
    private Double logConfidenceScore;
    private OffsetDateTime logCreatedAt;
}