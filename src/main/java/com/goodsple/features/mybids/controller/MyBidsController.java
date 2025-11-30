package com.goodsple.features.mybids.controller;

import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.mybids.dto.response.MyBidsResponse;
import com.goodsple.features.mybids.service.MyBidsService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "05. 나의 입찰/낙찰 (MyBids)", description = "나의 입찰/낙찰 내역 조회 API")
@RestController
@RequestMapping("/api/my-bids")
@RequiredArgsConstructor
public class MyBidsController {

    private final MyBidsService myBidsService;

    @Operation(summary = "나의 낙찰 내역 조회", description = "현재 로그인한 사용자가 낙찰받은 경매 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
    })
    @GetMapping("/won-auctions")
    public ResponseEntity<PagedResponse<MyBidsResponse>> getMyWonAuctions(
            Authentication authentication,
            @PageableDefault(size = 6) Pageable pageable) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        PagedResponse<MyBidsResponse> response = myBidsService.getMyWonAuctions(userId, pageable);
        return ResponseEntity.ok(response);
    }
}