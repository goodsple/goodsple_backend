package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "아이디 찾기 인증번호 요청")
public class FindIdCodeRequest {

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;
}
