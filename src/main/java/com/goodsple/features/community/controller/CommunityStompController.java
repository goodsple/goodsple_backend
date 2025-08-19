package com.goodsple.features.community.controller;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.service.CommunityService;
import com.goodsple.features.community.service.RoomValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class CommunityStompController {

    private final CommunityService communityService;
    private final SimpMessagingTemplate template;
    private final RoomValidator roomValidator;

    @MessageMapping("/join/{roomId}")
    public void join(@DestinationVariable String roomId) {
        roomValidator.ensureValid(roomId);
        // 필요 시 시스템 메시지 저장/알림 구현 가능
    }

    @MessageMapping("/sendPost/{roomId}")
    public void send(@DestinationVariable String roomId,
                     @Payload SendPostPayload payload,
                     Authentication auth) {

        roomValidator.ensureValid(roomId);

        // JWT에서 Principal 가져오기
        Long userId = null;
        Object principal = auth.getPrincipal();
        if (principal instanceof String) {
            userId = Long.parseLong((String) principal);
        } else if (principal instanceof Long) {
            userId = (Long) principal;
        }

        if (userId == null || payload.getContent() == null || payload.getContent().isBlank()) return;

        // 게시글 저장
        communityService.savePost(roomId, userId, payload.getContent());

        // 브로드캐스트용 DTO 구성
        Community brief = communityService.getUserInfo(userId);
        Community out = new Community();
        out.setRoomId(roomId);
        out.setUserId(userId);
        out.setContent(payload.getContent());
        out.setUserName(brief != null ? brief.getUserName() : "익명");
        out.setUserProfile(brief != null ? brief.getUserProfile() : null);
        out.setCreatedAt(Instant.now().toString());

        template.convertAndSend("/topic/" + roomId, out);
    }

    @Data
    public static class SendPostPayload {
        private String content;
    }
}
