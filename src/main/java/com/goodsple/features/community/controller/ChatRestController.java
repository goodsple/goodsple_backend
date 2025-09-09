package com.goodsple.features.community.controller;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.service.CommunityService;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final CommunityService communityService;

    // 특정 방 채팅 내역 조회
    @GetMapping("/history/{roomId}")
    public List<Community> history(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String before,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Instant pivot = null;
        if (before != null) {
            try {
                pivot = Instant.parse(before);
            } catch (java.time.format.DateTimeParseException e) {
                pivot = null;
            }
        }

        int safeLimit = Math.min(Math.max(1, limit), 100);
        return communityService.getPosts(roomId, safeLimit, pivot);
    }

    // 유저 정보 조회
    @GetMapping("/user/{userId}")
    public Community getUserInfo(@PathVariable Long userId) {
        return communityService.getUserInfo(userId);
    }

    // 현재 방 접속자 수 조회
    @GetMapping("/room/{roomId}/users")
    public int getRoomUserCount(@PathVariable String roomId) {
        return communityService.getRoomUserCount(roomId);
    }

    // 확성기 남은 횟수 조회
    @GetMapping("/megaphone-remaining")
    public int getMegaphoneRemaining(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) throw new IllegalStateException("인증 정보가 없습니다.");
        return communityService.getMegaphoneRemaining(userDetails.getUserId());
    }

    // 최신 확성기 메시지 1개 반환 메서드 ( 메인페이지 )
    @GetMapping("/megaphone/latest")
    public Community getLatestMegaphone() {
        return communityService.getLatestMegaphone();
    }

}
