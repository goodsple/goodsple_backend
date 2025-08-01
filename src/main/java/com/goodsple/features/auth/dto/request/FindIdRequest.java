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
@Schema(description = "아이디 찾기 인증번호 검증 및 아이디 반화")
public class FindIdRequest {

    @Schema(description = "회원 이름", example = "홍길동", required = true)
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(description = "회원 이메일", example = "user@example.com", required = true)
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "이메일로 받은 6자리 인증번호", example = "123456", required = true)
    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;
}
