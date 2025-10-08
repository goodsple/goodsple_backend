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

/**
 * STOMP WebSocket 컨트롤러
 * publish:  /app/chat/send, /app/chat/read
 * subscribe: /topic/chat.{roomId}
 */
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
        ChatMessage saved = chatService.send(req.roomId(), me, req.content());

        tmpl.convertAndSend("/topic/chat." + req.roomId(),
                Map.of(
                        "type", "message:new",
                        "data", Map.of(
                                "roomId", req.roomId(),
                                "message", Map.of(
                                        "id",        saved.getMessageId(),
                                        "senderId",  saved.getSenderId(),
                                        "content",   saved.getMessage(),
                                        "createdAt", saved.getChatMessageCreatedAt()
                                )
                        )
                )
        );
    }

    @Operation(summary = "[WS] 읽음 처리", description = "publish: /app/chat/read, subscribe: /topic/chat.{roomId}")
    @MessageMapping("/chat/read")
    public void read(ReadReq req) {
        Long me = auth.userId();
        chatService.read(req.roomId(), me, req.lastReadMessageId());

        tmpl.convertAndSend("/topic/chat." + req.roomId(),
                Map.of(
                        "type", "message:read",
                        "data", Map.of(
                                "roomId",             req.roomId(),
                                "userId",             me,
                                "lastReadMessageId",  req.lastReadMessageId()
                        )
                )
        );
    }
}
