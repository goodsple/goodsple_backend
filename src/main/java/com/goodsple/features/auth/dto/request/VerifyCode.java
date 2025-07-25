package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "전송받은 인증번호의 유효성을 검증")
public class VerifyCode {

    @NotBlank
    private String loginId;

    @Email
    private String email;

    @NotBlank
    private String code;
}
