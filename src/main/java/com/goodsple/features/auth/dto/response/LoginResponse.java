package com.goodsple.features.auth.dto.response;

import com.goodsple.features.user.dto.response.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema( description = "로그인 성공 시 클라이언트에 반환되는 응답 객체")
public class LoginResponse {

    @Schema(
            description = "발급된 액세스 토큰 (Bearer 스킴 없이 순수 JWT 값)",
            example     = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            required    = true
    )
    private String accessToken;

    @Schema(
            description = "발급된 리프레시 토큰",
            example     = "dGhpcyBpcyByZWZyZXNoIHRva2Vu...",
            required    = true
    )
    private String refreshToken;

    @Schema(
            description = "로그인한 사용자의 프로필 정보",
            required    = true
    )
    private UserProfile userProfile;
}
