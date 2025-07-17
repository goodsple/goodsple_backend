package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "로그인 요청 정보")
public class LoginRequest {

    @Schema(description = "로그인 아이디", example = "testuser123")
    private String loginId;

    @Schema(description = "비밀번호", example = "password123")
    private String password;
}
