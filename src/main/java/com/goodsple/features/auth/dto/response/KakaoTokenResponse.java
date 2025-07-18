package com.goodsple.features.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)  // 카카오가 주는 추가 필드는 무시
public class KakaoTokenResponse {
    // 카카오로부터 인가 코드를 사용해 발급받은 액세스 토큰, 리프레시 토큰 등의 정보를 담는 DTO

    @JsonProperty("access_token")
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @JsonProperty("token_type")
    @Schema(description = "토큰 타입(Bearer 등) ", example = "Bearer")
    private String tokenType;

    @JsonProperty("refresh_token")
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;

    @JsonProperty("expires_in")
    @Schema(description = "액세스 토큰 만료 시간(초)", example = "3600")
    private Long expiresIn;

    @JsonProperty("scope")
    @Schema(description = "스코프(권한 범위)", example = "account_email profile")
    private String scope;

    @JsonProperty("refresh_token_expires_in")
    @Schema(description = "리프레시 토큰 만료 시간(초)", example = "5184000")
    private Long refreshTokenExpiresIn;

}
