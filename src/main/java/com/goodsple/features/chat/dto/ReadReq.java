package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WS: 읽음 처리 요청")
public record ReadReq(
        @Schema(description = "채팅방 ID", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        Long roomId,
        @Schema(description = "마지막으로 읽은 메시지 ID(이 ID까지 읽음)", example = "4567", requiredMode = Schema.RequiredMode.REQUIRED)
        Long lastReadMessageId
) {}
