package com.goodsple.features.admin.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminChatLogDetailResponse {
    private Long logId;
    private String loginId;

    private String initialMessage;
    private String finalIntent;
    private Double confidence;

    private List<AdminChatMessageResponse> conversation;

    public void setConversation(List<AdminChatMessageResponse> conversation) {
        this.conversation = conversation;
    }
}
