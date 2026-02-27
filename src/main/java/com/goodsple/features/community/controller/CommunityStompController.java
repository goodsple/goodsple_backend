package com.goodsple.features.community.controller;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.service.CommunityService;
import com.goodsple.features.community.service.RoomValidator;
import com.goodsple.security.CustomUserDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class CommunityStompController {

    private final CommunityService communityService;
    private final SimpMessagingTemplate template;
    private final RoomValidator roomValidator;

    // 방 참여
    @MessageMapping("/join/{roomId}")
    public void join(@DestinationVariable String roomId,
                     StompHeaderAccessor accessor,
                     Authentication auth) {
        roomValidator.ensureValid(roomId);
        String sessionId = accessor.getSessionId();
        communityService.joinRoom(roomId, sessionId);


        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId();
        communityService.saveJoinLog(roomId, userId);
    }

    // 세션 종료(퇴장) 이벤트
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        communityService.getAllRooms().forEach(roomId -> communityService.leaveRoom(roomId, sessionId));
    }

    // 채팅 메시지 전송
    @MessageMapping("/sendPost/{roomId}")
    public void send(@DestinationVariable String roomId,
                     @Payload SendPostPayload payload,
                     Authentication auth) {

        roomValidator.ensureValid(roomId);

        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId();

        if (payload.getContent() == null || payload.getContent().isBlank()) return;

        // 게시글 저장 (type 포함)
        try {
            communityService.savePost(roomId, userId, payload.getContent(), payload.getType());
        } catch (IllegalStateException e) {
            // MEGAPHONE 한 달 제한 초과 시 프론트에 에러 전송
            Community errorMessage = new Community();
            errorMessage.setCommRoomId(roomId);
            errorMessage.setUserId(userId);
            errorMessage.setContent(e.getMessage());
            errorMessage.setType("ERROR");
            template.convertAndSend("/topic/" + roomId, errorMessage);
            return;
        }

        // 유저 기본 정보 가져오기
        Community brief = communityService.getUserInfo(userId);

        // 브로드캐스트용 DTO 생성
        Community out = new Community();
        out.setCommRoomId(roomId);
        out.setUserId(userId);
        out.setContent(payload.getContent());
        out.setType(payload.getType());
        out.setNickname(brief != null ? brief.getNickname() : "익명");
        out.setUserProfile(brief != null ? brief.getUserProfile() : null);
        out.setBadgeImageUrl(brief.getBadgeImageUrl());
        out.setBadgeName(brief.getBadgeName());
        out.setBadgeLevel(brief.getBadgeLevel());
        out.setCommCreatedAt(Instant.now().toString());

        // 클라이언트 전송
        template.convertAndSend("/topic/" + roomId, out);

        // 확성기 사용
        if("MEGAPHONE".equals(payload.getType())) {
            template.convertAndSend("/topic/megaphone", out);
        }
    }

    @Data
    public static class SendPostPayload {
        private String content;
        private String type; // "GENERAL" | "MEGAPHONE"
    }
}
