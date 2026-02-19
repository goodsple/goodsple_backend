package com.goodsple.features.admin.review.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReviewSearchCond {
    private String keyword;
    private List<String> statuses; // NORMAL | BLIND
    private String fromDate;
    private String toDate;
    private Integer page;
    private Integer size;
}
