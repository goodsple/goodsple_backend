package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "이메일 인증 코드 발급을 요청")
public class EmailSendCodeRequest {

    @Schema(
            description = "인증 코드를 전송할 이메일 주소",
            example     = "user@example.com",
            required    = true
    )
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(
            description = "인증 코드 요청 목적 (아이디 찾기: 'find-id', 비밀번호 재설정: 'reset-password')",
            allowableValues = {"find-id", "reset-password"},
            example     = "find-id",
            required    = true
    )
    @NotEmpty(message = "purpose를 입력해주세요.")
    @Pattern(
            regexp  = "find-id|reset-password",
            message = "purpose는 'find-id' 또는 'reset-password'만 허용됩니다."
    )
    private String purpose;
}
