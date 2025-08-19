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
                pivot = null; // 잘못된 값이면 무시
            }
        }

        int safeLimit = Math.min(Math.max(1, limit), 100);

        // DTO 필드명 기준으로 결과 반환
        return communityService.getPosts(roomId, safeLimit, pivot);
    }
}
