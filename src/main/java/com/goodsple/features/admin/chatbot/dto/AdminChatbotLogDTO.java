package com.goodsple.features.admin.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminChatbotLogDTO {

    private Long logId;
    private String logSessionId;

    private String loginId;

    private String logType;
    private String logInitialQuestion;
    private String logPredictedIntent;
    private Double logConfidenceScore;
    private OffsetDateTime logCreatedAt;
}
