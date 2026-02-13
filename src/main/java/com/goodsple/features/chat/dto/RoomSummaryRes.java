package com.goodsple.features.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "채팅방 요약 응답")
public record RoomSummaryRes(
        @Schema(description = "채팅방 ID", example = "12")
        Long roomId,

        @Schema(description = "상대 프로필")
        Peer peer,

        @Schema(description = "마지막 메시지(없을 수 있음)")
        LastMessage lastMessage,   // nullable

        @Schema(description = "내 안읽음 개수", example = "2")
        int unreadCount,

        @Schema(description = "최근 활동 시각(ISO)", example = "2025-10-09T12:10:00+09:00")
        String updatedAt,          // ISO 문자열

        @Schema(description = "게시글 프리뷰(없을 수 있음)")
        PostPreview postPreview    // nullable
) {
    @Schema(description = "상대 정보")
    public record Peer(
            @Schema(description = "상대 유저 ID", example = "25") Long userId,
            @Schema(description = "상대 닉네임", example = "굿또") String nickname,
            @Schema(description = "아바타 경로", example = "/u/25.png") String avatar,
            @Schema(description = "인증 여부", example = "true") Boolean verified,
            @Schema(description = "레벨 텍스트", example = "LV.2") String levelText,
            @Schema(description = "뱃지 이미지 URL", example = "/img/badge1.png") String badgeImageUrl
    ) {}

    @Schema(description = "마지막 메시지")
    public record LastMessage(
            @Schema(description = "메시지 ID", example = "321") Long messageId,
            @Schema(description = "본문", example = "내일 저녁 괜찮아요?") String text,
            @Schema(description = "작성 시각(ISO)", example = "2025-10-09T12:10:00+09:00") String createdAt
    ) {}

    @Schema(description = "게시글 프리뷰")
    public record PostPreview(
            @Schema(description = "제목", example = "엑스 백현 포카 교환") String title,
            @Schema(description = "썸네일", example = "/p/1.jpg") String thumb,
            @Schema(description = "태그", example = "[\"직거래\",\"택배거래\"]") List<String> tags
    ) {}
}
