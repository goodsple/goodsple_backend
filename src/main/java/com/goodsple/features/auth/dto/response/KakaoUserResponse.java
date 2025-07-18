package com.goodsple.features.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)   // 모르는 필드는 무시
@Schema(description = "카카오 사용자 정보 응답")
public class KakaoUserResponse {
    // 카카오 API를 통해 조회한 사용자 정보(이메일, 닉네임 등)를 담는 DTO

    @JsonProperty("id")
    @Schema(description = "카카오 유저 고유 ID", example = "123456789")
    private Long id;

    @JsonProperty("connected_at")
    @Schema(description = "카카오 계정 연결 시간", example = "2024-07-17T12:00:00Z")
    private String connectedAt;

    @JsonProperty("kakao_account")
    @Schema(description = "카카오 계정 정보")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "카카오 계정 상세 정보")
    public static class KakaoAccount {
        private Profile profile;

        @Schema(description = "카카오 계정 이메일", example = "user@example.com")
        private String email;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @Schema(description = "카카오 프로필 정보")
        public static class Profile {
            @Schema(description = "카카오 닉네임", example = "홍길동")
            private String nickname;

            @JsonProperty("thumbnail_image_url")
            @Schema(description = "카카오 썸네일 이미지 URL", example = "http://example.com/thumb.jpg")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            @Schema(description = "카카오 프로필 이미지 URL", example = "http://example.com/profile.jpg")
            private String profileImageUrl;
        }
    }
}
