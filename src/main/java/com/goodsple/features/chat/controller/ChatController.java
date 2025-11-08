package com.goodsple.features.chat.controller;

import com.goodsple.features.chat.dto.CreateRoomReq;
import com.goodsple.features.chat.dto.MessageRes;
import com.goodsple.features.chat.dto.RoomSummaryRes;
import com.goodsple.features.chat.dto.SendMessageReq;
import com.goodsple.features.chat.entity.ChatMessage;
import com.goodsple.features.chat.service.ChatService;
import com.goodsple.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final CurrentUser auth;

    @Operation(
            summary = "채팅방 생성(혹은 기존 방 재사용)",
            description = "같은 유저쌍(+동일 게시글)으로 요청 시 기존 roomId를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = RoomIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 오류",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @PostMapping("/rooms")
    public Map<String, Long> createRoom(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "상대 사용자/게시글 정보",
                    content = @Content(schema = @Schema(implementation = CreateRoomReq.class))
            )
            @RequestBody CreateRoomReq req
    ) {
        Long me = auth.userId();
        Long roomId = chatService.createOrGetRoom(me, req.peerId(), req.postId());
        return Map.of("roomId", roomId);
    }

    @Operation(
            summary = "메시지 페이지 조회",
            description = "최신부터 위로 스크롤하는 키셋 페이지네이션. 응답에 내/상대 읽음 커서 포함."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessagesPageResponse.class)
            )),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음(방 비참여자)",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @GetMapping("/rooms/{roomId}/messages")
    public Map<String, Object> messages(
            @Parameter(description = "방 ID", example = "1")
            @PathVariable Long roomId,
            @Parameter(description = "해당 ID 미만만 조회(키셋 페이지네이션)", example = "1001")
            @RequestParam(required = false) Long beforeId,
            @Parameter(description = "가져올 개수(기본 50)", example = "50")
            @RequestParam(defaultValue = "50") int limit
    ) {
        Long me = auth.userId();

        List<MessageRes> messages = chatService.load(roomId, beforeId, limit)
                .stream().map(this::toRes).toList();

        Map<String, Long> cursors = chatService.cursors(roomId, me);

        return Map.of(
                "messages", messages,
                "myLastReadMessageId",   cursors.get("myLastReadMessageId"),
                "peerLastReadMessageId", cursors.get("peerLastReadMessageId")
        );
    }

    private MessageRes toRes(ChatMessage m) {
        return new MessageRes(
                m.getMessageId(),
                m.getSenderId(),
                m.getMessage(),
                m.getChatMessageCreatedAt()
        );
    }

    @Operation(
            summary = "내 채팅방 요약 리스트",
            description = "내가 참여 중인 모든 채팅방의 요약(상대 프로필, 마지막 메시지, 안읽음 수, 정렬기준 시간)을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomSummaryRes.class)))),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @GetMapping("/rooms/summary")
    public List<RoomSummaryRes> summaries(
            @Parameter(description = "페이지 크기(최대 100)", example = "50")
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "페이지 번호(0부터)", example = "0")
            @RequestParam(defaultValue = "0") int page
    ) {
        Long me = auth.userId();
        int limit = Math.max(1, Math.min(100, size));
        int offset = Math.max(0, page) * limit;
        return chatService.listSummaries(me, limit, offset);
    }

    @Operation(
            summary = "메시지 전송",
            description = "내가 참여 중인 채팅방에 텍스트 메시지를 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = MessageRes.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 오류",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음(방 비참여자)",
                    content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @PostMapping("/messages")
    public MessageRes sendMessage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "전송할 방/텍스트",
                    content = @Content(schema = @Schema(implementation = SendMessageReq.class))
            )
            @Valid @RequestBody SendMessageReq req
    ) {
        Long me = auth.userId();
        // 서비스는 ChatMessage 엔티티를 반환한다고 가정 (없으면 messageId만 반환해도 무방)
        com.goodsple.features.chat.entity.ChatMessage saved = chatService.sendMessage(me, req.roomId(), req.text());
        return toRes(saved);
    }


    // ===== 문서 스키마용 작은 레코드들 =====

    @Schema(description = "roomId 응답")
    public record RoomIdResponse(
            @Schema(description = "채팅방 ID", example = "1") Long roomId
    ) {}

    @Schema(description = "메시지 페이지 응답")
    public record MessagesPageResponse(
            @ArraySchema(arraySchema = @Schema(description = "메시지 목록"),
                    schema = @Schema(implementation = MessageRes.class))
            List<MessageRes> messages,
            @Schema(description = "내 읽음 커서", example = "1020") Long myLastReadMessageId,
            @Schema(description = "상대 읽음 커서", example = "1010") Long peerLastReadMessageId
    ) {}

    @Schema(description = "오류 응답")
    public record ErrorRes(
            @Schema(description = "에러 코드", example = "CHAT-403") String code,
            @Schema(description = "메시지", example = "이 방의 참여자가 아닙니다.") String message
    ) {}

    @PostMapping("/read")
    public Map<String, Object> read(@RequestBody Map<String, Long> body) {
        Long me = auth.userId();
        Long roomId = body.get("roomId");
        Long lastReadMessageId = body.get("lastReadMessageId"); // ← 키 이름 중요

        if (roomId == null || lastReadMessageId == null) {
            throw new IllegalArgumentException("roomId and lastReadMessageId are required");
        }

        chatService.read(roomId, me, lastReadMessageId);
        return Map.of("ok", true);
    }

}
