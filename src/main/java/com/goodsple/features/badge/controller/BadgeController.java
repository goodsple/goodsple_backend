package com.goodsple.features.badge.controller;

import com.goodsple.features.badge.dto.MyScoreResponseDto;
import com.goodsple.features.badge.service.BadgeService;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badge")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/myscores")
    public ResponseEntity<MyScoreResponseDto> getMyScore(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(badgeService.getMyScore(userDetails.getUserId()));
    }

    // 거래 완료 시 호출
    @PostMapping("/trade-complete/{userId}/{postId}/{fast}")
    public ResponseEntity<String> rewardTrade(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @PathVariable boolean fast
    ) {

        badgeService.rewardTradeComplete(userId, postId, fast);

        return ResponseEntity.ok("점수 적립 완료");
    }


}
