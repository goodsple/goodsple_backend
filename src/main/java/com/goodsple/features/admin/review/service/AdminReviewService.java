package com.goodsple.features.admin.review.service;

import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.admin.review.dto.AdminReviewDetail;
import com.goodsple.features.admin.review.dto.AdminReviewListItem;
import com.goodsple.features.admin.review.dto.AdminReviewSearchCond;
import com.goodsple.features.admin.review.mapper.AdminReviewMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewMapper mapper;

    @Transactional(readOnly = true)
    public PagedResponse<AdminReviewListItem> list(AdminReviewSearchCond cond) {
        normalizeStatuses(cond);
        int page = cond.getPage() == null ? 0 : Math.max(0, cond.getPage());
        int size = cond.getSize() == null ? 20 : Math.max(1, Math.min(100, cond.getSize()));
        int offset = page * size;

        List<AdminReviewListItem> content = mapper.selectReviews(cond, size, offset);
        long total = mapper.countReviews(cond);
        int totalPages = (int) Math.ceil(total / (double) size);
        return new PagedResponse<>(content, page, totalPages, total);
    }

    @Transactional(readOnly = true)
    public AdminReviewDetail detail(Long reviewId) {
        AdminReviewDetail detail = mapper.selectReviewDetail(reviewId);
        if (detail != null) {
            detail.setPhotos(mapper.selectReviewPhotos(reviewId));
        }
        return detail;
    }

    @Transactional
    public void updateStatus(Long reviewId, String status) {
        mapper.updateReviewStatus(reviewId, toDbStatus(status));
    }

    private void normalizeStatuses(AdminReviewSearchCond cond) {
        if (cond == null || cond.getStatuses() == null) return;
        cond.setStatuses(cond.getStatuses().stream()
                .map(this::toDbStatus)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .toList());
    }

    private String toDbStatus(String status) {
        if (status == null) return null;
        String s = status.trim().toUpperCase();
        return switch (s) {
            case "NORMAL" -> "visible";
            case "BLIND" -> "hidden";
            default -> status;
        };
    }
}
