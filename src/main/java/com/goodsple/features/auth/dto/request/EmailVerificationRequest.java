package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "이메일과 인증 코드를 검증하기 위한 요청")
public class EmailVerificationRequest {

    @NotEmpty(message = "이메일을 입력해주세요.") // 이메일 값이 비어 있지 않은지 확인
    @Email(message = "올바른 이메일 형식이 아닙니다.") // 이메일 형식 검증
    private String email;

    @NotEmpty(message = "인증 코드를 입력해주세요.") // 인증 코드가 비어 있지 않도록 검증
    private String code;
}
