package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.response.KakaoTokenResponse;
import com.goodsple.features.auth.dto.response.KakaoUserResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /** 카카오 인증 URL 생성 */
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .build().toUriString();
    }

    /** 카카오 콜백 코드로 로그인 처리 */
    public TokenResponse loginWithKakao(String code) {
        // 1) 코드 → 카카오 액세스 토큰 교환
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("code", code);
        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);

        KakaoTokenResponse kakaoToken = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenEntity,
                KakaoTokenResponse.class
        ).getBody();

        // 2) 카카오 API로 유저 정보 조회
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.setBearerAuth(kakaoToken.getAccessToken());
        HttpEntity<Void> profileEntity = new HttpEntity<>(profileHeaders);

        KakaoUserResponse kakaoUser = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileEntity,
                KakaoUserResponse.class
        ).getBody();

        // 3) DB에 이미 가입된 카카오 유저인지 확인
        String kakaoId = kakaoUser.getId().toString();
        User existing = userMapper.findByKakaoId(kakaoId);
        if (existing == null) {
            // 신규 유저라면 회원가입
            User newUser = User.builder()
                    .loginId("kakao_" + kakaoId)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .name(kakaoUser.getKakaoAccount().getProfile().getNickname())
                    .email(kakaoUser.getKakaoAccount().getEmail())
                    .kakaoId(kakaoId)
                    .role(Role.USER)
                    .build();
            userMapper.createUser(newUser);
            existing = userMapper.findByLoginId(newUser.getLoginId());
        }

        // 4) JWT 발급
        String at = jwtProvider.createAccessToken(existing.getUserId(), existing.getRole().name());
        String rt = jwtProvider.createRefreshToken(existing.getUserId(), existing.getRole().name());
        return TokenResponse.builder()
                .accessToken(at)
                .refreshToken(rt)
                .build();
    }
}
