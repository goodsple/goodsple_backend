package com.goodsple.features.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long messageId;
    private String message;                 // 컬럼명: message
    private Instant chatMessageCreatedAt;   // 컬럼명: chat_message_created_at
    private Long chatRoomId;
    private Long senderId;
}
