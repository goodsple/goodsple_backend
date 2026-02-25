package com.goodsple.features.chat.vo;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class RoomSummaryRow {
    private Long roomId;

    private Long    peerUserId;
    private String  peerNickname;
    private String  peerAvatar;
    private Boolean peerVerified;
    private String  peerLevelText;
    private String  peerBadgeImageUrl;

    private Long          lastMessageId;
    private String        lastText;
    private OffsetDateTime lastCreatedAt;

    private Integer       unreadCount;
    private OffsetDateTime updatedAt;
}
