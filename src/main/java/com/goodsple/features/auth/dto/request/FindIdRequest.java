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
@Schema(description = "아이디 찾기 인증번호 검증 및 아이디 조회")
public class FindIdRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;
}
