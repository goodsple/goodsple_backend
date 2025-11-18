package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "메시지 응답")
public record MessageRes(
        @Schema(description = "메시지 ID", example = "1001") Long id,
        @Schema(description = "보낸 사람 ID", example = "10") Long senderId,
        @Schema(description = "본문", example = "안녕하세요!") String content,
        @Schema(description = "보낸 시각(UTC)", example = "2025-07-01T02:31:00Z") Instant createdAt
) {}
