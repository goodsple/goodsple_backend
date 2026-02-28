package com.goodsple.features.admin.dashboard.controller;

import com.goodsple.features.admin.dashboard.dto.*;
import com.goodsple.features.admin.dashboard.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService service;

    @Operation(summary = "관리자 대시보드 회원 통계")
    @GetMapping("/users")
    public ResponseEntity<AdminUserStatsResponse> userStats(
            @RequestParam(defaultValue = "3") int months
    ) {
        int safeMonths = Math.max(1, Math.min(12, months));
        return ResponseEntity.ok(service.getUserStats(safeMonths));
    }

    @Operation(summary = "관리자 대시보드 신고 통계")
    @GetMapping("/reports")
    public ResponseEntity<AdminReportStatsResponse> reportStats(
            @RequestParam(defaultValue = "3") int months
    ) {
        int safeMonths = Math.max(1, Math.min(12, months));
        return ResponseEntity.ok(service.getReportStats(safeMonths));
    }

    @Operation(summary = "관리자 대시보드 인기검색어 통계")
    @GetMapping("/popular-keywords")
    public ResponseEntity<AdminPopularKeywordStatsResponse> popularKeywordStats() {
        return ResponseEntity.ok(service.getPopularKeywordStats());
    }

    @GetMapping("/community")
    public ResponseEntity<AdminCommunityStatsResponse> communityStats(
            @RequestParam(defaultValue = "3") int months
    ) {
        int safeMonths = Math.max(1, Math.min(12, months));
        return ResponseEntity.ok(service.getCommunityStats(safeMonths));
    }

    @Operation(summary = "관리자 대시보드 경매 통계")
    @GetMapping("/auctions")
    public ResponseEntity<AdminAuctionStatsResponse> auctionStats() {
        return ResponseEntity.ok(service.getAuctionStats());
    }

}
