package com.goodsple.features.report.controller;

import com.goodsple.features.report.dto.request.Report;
import com.goodsple.features.report.dto.request.ReportReason;
import com.goodsple.features.report.service.ReportService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "신고 등록 및 조회 API")
public class ReportController {

    private final ReportService reportService;

    /**
     * 신고 등록
     */
    @Operation(summary = "신고 등록", description = "신고 내역과 사유 ID 리스트를 입력받아 새 신고를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신고가 성공적으로 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증 오류입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @PostMapping
    public ResponseEntity<Void> createReport(
            Authentication authentication,
            @RequestBody Report request,
            @RequestParam List<Long> reasonIds
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails me = (CustomUserDetails) authentication.getPrincipal();
        Long userId = me.getUserId();          // ← 신고자 ID
        request.setReporterId(userId);
        reportService.createReport(request, reasonIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 전체 신고 사유 옵션 조회
     */
    @Operation(summary = "신고 사유 리스트 조회", description = "사용 가능한 신고 사유 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 사유 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @GetMapping("/reasons")
    public ResponseEntity<List<ReportReason>> getReasons() {
        List<ReportReason> reasons = reportService.getAllReasons();
        return ResponseEntity.ok(reasons);
    }

    @Operation(summary = "신고 여부 확인", description = "현재 로그인 사용자가 대상에 대해 신고했는지 확인합니다.")
    @GetMapping("/check")
    public ResponseEntity<?> checkReported(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam String targetType,
            @RequestParam Long targetId
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean reported = reportService.hasReported(user.getUserId(), targetType, targetId);
        return ResponseEntity.ok().body(java.util.Map.of("reported", reported));
    }

    /**
     * ID로 신고 조회
     */
    @Operation(summary = "신고 상세 조회", description = "신고 ID로 저장된 신고 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 조회 성공"),
            @ApiResponse(responseCode = "404", description = "신고를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(
            @PathVariable("id") Long reportId
    ) {
        Report report = reportService.getReportById(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }
}
