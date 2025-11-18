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
public class ChatParticipant {
    private Long chatRoomId;            // PK part 1
    private Long userId;                // PK part 2
    private Long lastReadMessageId;     // 읽음 커서
    private Instant joinedAt;
    private Instant leftAt;
}
