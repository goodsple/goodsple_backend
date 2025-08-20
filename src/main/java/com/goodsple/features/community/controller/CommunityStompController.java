package com.goodsple.features.community.controller;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.service.CommunityService;
import com.goodsple.features.community.service.RoomValidator;
import com.goodsple.security.CustomUserDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    // 방 참여
    @MessageMapping("/join/{roomId}")
    public void join(@DestinationVariable String roomId) {
        roomValidator.ensureValid(roomId);
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

        // DB 저장
        communityService.savePost(roomId, userId, payload.getContent());

        // 유저 기본 정보 가져오기
        Community brief = communityService.getUserInfo(userId);

        // 브로드캐스트용 DTO 생성
        Community out = new Community();
        out.setCommRoomId(roomId);
        out.setUserId(userId);
        out.setContent(payload.getContent());
        out.setNickname(brief != null ? brief.getNickname() : "익명"); // 수정
        out.setUserProfile(brief != null ? brief.getUserProfile() : null);
        out.setCommCreatedAt(Instant.now().toString());

        // 클라이언트 전송
        template.convertAndSend("/topic/" + roomId, out);
    }

    @Data
    public static class SendPostPayload {
        private String content;
    }
}
