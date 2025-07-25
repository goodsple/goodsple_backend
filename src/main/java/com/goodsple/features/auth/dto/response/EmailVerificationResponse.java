package com.goodsple.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "인증 코드와 관련된 이메일 인증 정보")
public class EmailVerificationResponse {

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "인증 코드", example = "123456")
    private String code;

    @Schema(description = "인증 코드 생성 일시", example = "2025-07-25T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "인증 코드 만료 일시", example = "2025-07-25T12:05:00")
    private LocalDateTime expiresAt;

    @Schema(description = "인증 코드 사용 여부", example = "false")
    private boolean used;
}
