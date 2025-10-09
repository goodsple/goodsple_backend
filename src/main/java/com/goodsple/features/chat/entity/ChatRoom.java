package com.goodsple.features.chat.entity;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    private Long chatRoomId;
    private Instant chatRoomCreatedAt;
    private Long exchangePostId;
    private Long user1Id;
    private Long user2Id;
}
