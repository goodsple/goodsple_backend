package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "메시지 전송 요청")
public record SendMessageReq(
        @NotNull
        @Schema(description = "채팅방 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long roomId,

        @NotBlank
        @Size(min = 1, max = 1000)
        @Schema(description = "보낼 텍스트(1~1000자)", example = "안녕! 테스트 메시지", maxLength = 1000, requiredMode = Schema.RequiredMode.REQUIRED)
        String text
) {}
