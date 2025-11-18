package com.goodsple.features.chat.controller;

import com.goodsple.features.chat.dto.ReadReq;
import com.goodsple.features.chat.dto.SendReq;
import com.goodsple.features.chat.entity.ChatMessage;
import com.goodsple.features.chat.service.ChatService;
import com.goodsple.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate tmpl;
    private final CurrentUser auth;

    @Operation(summary = "[WS] 메시지 전송", description = "publish: /app/chat/send, subscribe: /topic/chat.{roomId}")
    @MessageMapping("/chat/send")
    public void send(SendReq req) {
        Long me = auth.userId();

        // 서비스 시그니처에 맞게 호출 (content -> text)
        ChatMessage saved = chatService.sendMessage(me, req.roomId(), req.content());

        // 💡 상대방 userId 찾기 (writer/buyer 반대쪽)
        Long peerId = chatService.findPeerId(req.roomId(), me);

        Map<String, Object> evt = Map.of(
                "type", "message:new",
                "data", Map.of(
                        "roomId", req.roomId(),
                        "message", Map.of(
                                "id",        saved.getMessageId(),
                                "senderId",  saved.getSenderId(),
                                // 프론트는 message|content|text 중 아무거나 읽음 → 일관 보강
                                "message",   saved.getMessage(),
                                "content",   saved.getMessage(),
                                "text",      saved.getMessage(),
                                "createdAt", saved.getChatMessageCreatedAt()
                        )
                )
        );

        // 1) 방 토픽
        tmpl.convertAndSend("/topic/chat." + req.roomId(), evt);

        // 2) 상대 유저 토픽 (좌측 리스트/배지 갱신용)
        if (peerId != null) {
            tmpl.convertAndSend("/topic/chat.user." + peerId, evt);
        }
    }

    @Operation(summary = "[WS] 읽음 처리", description = "publish: /app/chat/read, subscribe: /topic/chat.{roomId}")
    @MessageMapping("/chat/read")
    public void read(ReadReq req) {
        Long me = auth.userId();

        // 1) DB 커서 전진 (후퇴 금지)
        chatService.read(req.roomId(), me, req.lastReadMessageId());

        // 2) 상대방 userId 찾기
        Long peerId = chatService.findPeerId(req.roomId(), me);

        Map<String, Object> evt = Map.of(
                "type", "message:read",
                "data", Map.of(
                        "roomId",            req.roomId(),
                        "userId",            me,
                        "lastReadMessageId", req.lastReadMessageId()
                )
        );

        // 3) 방 토픽 (열린 채팅창의 버블/안읽음 표시 보정)
        tmpl.convertAndSend("/topic/chat." + req.roomId(), evt);

        // 4) 상대 유저 토픽 (좌측 리스트의 안읽음 카운트 보정)
        if (peerId != null) {
            tmpl.convertAndSend("/topic/chat.user." + peerId, evt);
        }
    }
}
