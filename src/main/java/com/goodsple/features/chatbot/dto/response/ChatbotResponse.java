package com.goodsple.features.chatbot.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatbotResponse {
    private Long logId;

    private String answer;
    private String intent;
    private double confidence;
}
