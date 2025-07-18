package com.goodsple.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "카카오 로그인 응답")
public class KakaoLoginResponse {

    @Schema(description = "신규 유저 여부", example = "true")
    private boolean isNewUser;

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;    // 기존 유저일 경우에만 값 세팅

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;   // 기존 유저일 경우에만 값 세팅

    // 신규 유저일 경우 필요한 추가 정보
    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "둥둥이")
    private String nickname;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "카카오 고유 ID", example = "123456789")
    private String kakaoId;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "성별", example = "male")
    private String gender;
}
