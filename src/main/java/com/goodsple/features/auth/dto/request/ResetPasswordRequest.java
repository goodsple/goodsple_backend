package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 찾기 인증번호 검증")
public class ResetPasswordRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @Schema(description = "이메일", example = "test@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}
