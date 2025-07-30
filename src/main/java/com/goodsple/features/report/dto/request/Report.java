package com.goodsple.features.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Long reportId;
    private Long reporterId;
    private Long reportTargetUserId;
    private String targetType;
    private Long targetId;
    private String reportStatus;
    private String actionTaken;
    private LocalDateTime reportCreatedAt;
    private LocalDateTime processedAt;
    private String reportDescription;
}
