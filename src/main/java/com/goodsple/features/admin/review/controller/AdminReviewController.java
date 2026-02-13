package com.goodsple.features.admin.review.controller;

import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.admin.review.dto.AdminReviewDetail;
import com.goodsple.features.admin.review.dto.AdminReviewSearchCond;
import com.goodsple.features.admin.review.dto.AdminReviewStatusUpdate;
import com.goodsple.features.admin.review.dto.AdminReviewListItem;
import com.goodsple.features.admin.review.service.AdminReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService service;

    @Operation(summary = "후기 목록 조회(관리자)")
    @GetMapping
    public ResponseEntity<PagedResponse<AdminReviewListItem>> list(@ModelAttribute AdminReviewSearchCond cond) {
        return ResponseEntity.ok(service.list(cond));
    }

    @Operation(summary = "후기 상세 조회(관리자)")
    @GetMapping("/{reviewId}")
    public ResponseEntity<AdminReviewDetail> detail(@PathVariable Long reviewId) {
        AdminReviewDetail detail = service.detail(reviewId);
        if (detail == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detail);
    }

    @Operation(summary = "후기 상태 변경(관리자)")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long reviewId,
                                             @RequestBody AdminReviewStatusUpdate req) {
        service.updateStatus(reviewId, req.getStatus());
        return ResponseEntity.noContent().build();
    }
}
