package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅방 생성 요청")
public record CreateRoomReq(
        @Schema(description = "상대 사용자 ID", example = "22", requiredMode = Schema.RequiredMode.REQUIRED)
                            Long peerId,
        @Schema(description = "게시글 ID (없으면 null)", example = "101")
                            Long postId
) {}
