package com.goodsple.features.community.controller;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final CommunityService communityService;

    @GetMapping("/history/{roomId}")
    public List<Community> history(@PathVariable String roomId,
                                   @RequestParam(defaultValue = "50") int limit,
                                   @RequestParam(required = false) String before) {
        Instant pivot = null;
        if (before != null) {
            try {
                pivot = Instant.parse(before);
            } catch (java.time.format.DateTimeParseException e) {
                // 잘못된 값이면 null 처리
                pivot = null;
            }
        }

        // limit 방어
        int safeLimit = Math.min(Math.max(1, limit), 100);

        return communityService.getPosts(roomId, safeLimit, pivot);
    }
}
