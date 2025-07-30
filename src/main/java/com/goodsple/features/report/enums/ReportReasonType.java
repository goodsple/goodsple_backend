package com.goodsple.features.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportReasonType {
    UNCOMFORTABLE("불쾌한 행동 및 언행"),
    SUSPECTED_FRAUD("사기 의심 거래"),
    INAPPROPRIATE_CONTENT("부적절한 게시글 또는 사진"),
    OTHER("기타");

    private final String description;
}
