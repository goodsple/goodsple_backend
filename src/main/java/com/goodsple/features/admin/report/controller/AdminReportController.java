package com.goodsple.features.admin.report.controller;

import com.goodsple.features.admin.report.dto.AdminReportDetail;
import com.goodsple.features.admin.report.dto.AdminReportList;
import com.goodsple.features.admin.report.dto.AdminReportSearchCond;
import com.goodsple.features.admin.report.dto.AdminReportStatusUpdate;
import com.goodsple.features.admin.report.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Reports", description = "관리자 신고 관리 API")
@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final AdminReportService service;

    @Operation(summary = "신고 목록 조회/검색", description = "조건 검색 및 페이징 지원")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = AdminReportList.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @GetMapping
    public AdminReportList list(
            @Parameter(description = "검색/필터 조건") AdminReportSearchCond cond
    ) {
        return service.getReportList(cond);
    }

    @Operation(summary = "신고 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = AdminReportDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @GetMapping("/{reportId}")
    public AdminReportDetail detail(@PathVariable Long reportId) {
        return service.getReportDetail(reportId);
    }

    @Operation(summary = "신고 상태 변경(처리/반려 등)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공(내용 없음)"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 상태값 또는 요청 본문"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @PatchMapping("/{reportId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long reportId,
            @RequestBody @Valid AdminReportStatusUpdate request
    ) {
        service.updateStatus(reportId, request);
        return ResponseEntity.noContent().build();
    }
}
