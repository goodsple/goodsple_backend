package com.goodsple.features.admin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "관리자 회원 목록 요약 DTO")
public class AdminUserSummary {

    @Schema(description = "회원 ID", example = "101")
    private Long userId;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "권한", example = "user") // user | admin
    private String role;

    @Schema(description = "활동 상태", example = "active") // active | suspended | withdrawn
    private String suspensionStatus;

    @Schema(description = "작성 후기 수", example = "12")
    private Integer reviewCount;

    @Schema(description = "거래 횟수", example = "8")
    private Integer tradeCount;

    @Schema(description = "신고 횟수", example = "1")
    private Integer reportCount;

    @Schema(description = "가입일", example = "2025-07-01T12:34:56+09:00")
    private OffsetDateTime joinedAt;

}
