package com.goodsple.features.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    /** 액세스 토큰 */
    @JsonProperty("access_token")
    private String accessToken;

    /** 토큰 타입 (Bearer 등) */
    @JsonProperty("token_type")
    private String tokenType;

    /** 리프레시 토큰 */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /** 액세스 토큰 만료 시간(초) */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /** 스코프(권한 범위) */
    @JsonProperty("scope")
    private String scope;

    /** 리프레시 토큰 만료 시간(초) */
    @JsonProperty("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;

}
