package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class ChatMessageResponse {
    private String type = "CHAT_MESSAGE";
    private String senderNickname;
    private String message;
    private OffsetDateTime timestamp;
}