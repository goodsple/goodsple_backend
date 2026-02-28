package com.goodsple.features.chat.controller;

import com.goodsple.features.admin.prohibitedWord.service.ProhibitedWordService;
import com.goodsple.features.chat.dto.ReadReq;
import com.goodsple.features.chat.dto.SendReq;
import com.goodsple.features.chat.entity.ChatMessage;
import com.goodsple.features.chat.service.ChatService;
import com.goodsple.security.CurrentUser;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate tmpl;
    private final CurrentUser auth;
    private final ProhibitedWordService prohibitedWordService;

    @Operation(summary = "[WS] 메시지 전송", description = "publish: /app/chat/send, subscribe: /topic/chat.{roomId}")
    @MessageMapping("/chat/send")
    public void send(SendReq req, Principal principal) {

        /*
         * WebSocket에서는 SecurityContext가 HTTP와 다르게 자동 보장되지 않을 수 있음.
         * 따라서 Principal이 null인지 먼저 체크
         */
        if (principal == null) {
            log.error("WebSocket principal is null");
            return;
        }

        /*
         * Principal → Authentication → CustomUserDetails 순으로 캐스팅하여
         * 현재 로그인한 사용자의 userId를 안전하게 추출
         *
         * HTTP Controller에서는 @AuthenticationPrincipal 등을 사용할 수 있지만,
         * STOMP(@MessageMapping)에서는 Principal을 직접 꺼내는 방식이 안전하다.
         */
        Authentication authentication = (Authentication) principal;
        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        // 현재 로그인 사용자 ID
        Long me = user.getUserId();

        // 1) 메시지 저장
        // 서비스 시그니처에 맞게 호출 (content -> text)
//        ChatMessage saved = chatService.sendMessage(me, req.roomId(), req.content());

        ChatMessage saved;
        try {
            saved = chatService.sendMessage(me, req.roomId(), req.content());
        } catch (IllegalArgumentException e) {
            // 🚨 서버 500 발생 방지, 프론트에 에러 메시지만 전달
            tmpl.convertAndSendToUser(
                    principal.getName(),
                    "/queue/errors",
                    Map.of(
                            "type", "error",
                            "message", e.getMessage() // "금칙어 포함: 바보멍청이"
                    )
            );
            return; // 처리 종료
        }

        // 2) 상대방 userId 찾기 (writer/buyer 중 나(me)가 아닌 사람을 peer로 계산)
        Long peerId = chatService.findPeerId(req.roomId(), me);

        // 3) 프론트로 전달할 이벤트 payload 구성
        Map<String, Object> evt = Map.of(
                "type", "message:new",
                "data", Map.of(
                        "roomId", req.roomId(),
                        "message", Map.of(
                                "id", saved.getMessageId(),
                                "senderId", saved.getSenderId(),
                                // 프론트는 message|content|text 중 아무거나 읽음 → 일관 보강
                                "message", saved.getMessage(),
                                "content", saved.getMessage(),
                                "text", saved.getMessage(),
                                "createdAt", saved.getChatMessageCreatedAt()
                        )
                )
        );

            // 4) 방 토픽 (현재 열려있는 채팅창의 말풍선 표시용)
            tmpl.convertAndSend("/topic/chat." + req.roomId(), evt);

            // 5) 상대 유저 토픽 (좌측 리스트/배지/안읽음 갱신용)
            if (peerId != null) {
                tmpl.convertAndSend("/topic/chat.user." + peerId, evt);
            }


    }

    @Operation(summary = "[WS] 읽음 처리", description = "publish: /app/chat/read, subscribe: /topic/chat.{roomId}")
    @MessageMapping("/chat/read")
    public void read(ReadReq req, Principal principal) {
        log.info("READ RECEIVED room={} last={}", req.roomId(), req.lastReadMessageId());
        System.out.println("READ HANDLER ENTER");
//        System.out.println("principal = " + user);

        /*
         * WebSocket에서는 HTTP와 다르게 SecurityContext가 자동 보장되지 않을 수 있다.
         * 따라서 Principal이 null인지 반드시 체크해야 한다.
         * (Handshake → ChannelInterceptor → MessageMapping 흐름)
         */
        if (principal == null) {
            log.error("WebSocket principal is null");
            return;
        }

        /*
         * Principal → Authentication → CustomUserDetails 순으로 캐스팅하여
         * 현재 로그인한 사용자의 userId를 추출한다.
         *
         * HTTP Controller에서는 @AuthenticationPrincipal 등을 사용할 수 있지만,
         * STOMP(@MessageMapping)에서는 Principal을 직접 꺼내는 방식이 안전하다.
         */
        Authentication authentication = (Authentication) principal;
        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        // 현재 로그인 사용자 ID
        Long me = user.getUserId();

        // 1) DB 커서 전진 (후퇴 금지)
        chatService.read(req.roomId(), me, req.lastReadMessageId());

        // 2) 상대방 userId 찾기
        Long peerId = chatService.findPeerId(req.roomId(), me);
        log.info("READ peerId={} me={}", peerId, me);

        // 3) 프론트로 전달할 read 이벤트 payload 구성
        // 누가(userId) 어디까지(lastReadMessageId) 읽었는지 전달
        Map<String, Object> evt = Map.of(
                "type", "message:read",
                "data", Map.of(
                        "roomId",            req.roomId(),
                        "userId",            me,
                        "lastReadMessageId", req.lastReadMessageId()
                )
        );

        // 4) 방 토픽 (열린 채팅창의 버블/안읽음 표시 보정)
        tmpl.convertAndSend("/topic/chat." + req.roomId(), evt);

        // 5) 상대 유저 토픽 (좌측 리스트의 안읽음 카운트 보정)
        if (peerId != null) {
            tmpl.convertAndSend("/topic/chat.user." + peerId, evt);
        }
    }
}
