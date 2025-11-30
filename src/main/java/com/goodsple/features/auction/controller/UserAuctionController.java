package com.goodsple.features.auction.controller;

import com.goodsple.features.auction.dto.response.UserMainPageResponseDto;
import com.goodsple.features.auction.service.UserAuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04. 메인 페이지 (User)", description = "사용자 메인 페이지 관련 API")
@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class UserAuctionController {

    private final UserAuctionService userAuctionService;

    @Operation(summary = "메인 페이지 대표 경매 조회", description = "사용자 메인 페이지에 필요한 대표 경매 1개를 조회합니다.")
    @GetMapping("/auction")
    public ResponseEntity<UserMainPageResponseDto> getMainPageAuction() {
        UserMainPageResponseDto response = userAuctionService.getMainPageAuction();
        return ResponseEntity.ok(response);
    }
}