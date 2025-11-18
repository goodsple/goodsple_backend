package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WS: 채팅 메시지 전송 요청")
public record SendReq(
        @Schema(description = "채팅방 ID", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        Long roomId,
        @Schema(description = "메시지 본문", example = "안녕하세요!", requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {}
