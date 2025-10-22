package com.goodsple.features.chatbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    private String answer;
    private String intent;
    private double confidence;
}
