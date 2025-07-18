package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.response.KakaoLoginResponse;
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


    /**
     * 카카오 로그인 요청 URL을 생성하는 메서드
     * 클라이언트에서 로그인 버튼 클릭 시 이 URL로 이동한다.
     */
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .build().toUriString();
    }

    /**
     * 카카오에서 전달받은 인가 코드를 사용해 토큰과 사용자 정보를 가져온 뒤,
     * 신규/기존 유저를 판별하여 응답을 반환하는 핵심 메서드
     */
    public KakaoLoginResponse loginWithKakao(String code) {
        System.out.println("받은 code = " + code);
        System.out.println("clientId = " + clientId);
        System.out.println("redirectUri = " + redirectUri);

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("code", code);
        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);

        // 인가 코드를 사용하여 카카오로부터 액세스 토큰을 요청하고, 응답으로 KakaoTokenResponse 객체를 받는다.
        KakaoTokenResponse kakaoToken = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenEntity,
                KakaoTokenResponse.class
        ).getBody();

        // 발급받은 액세스 토큰을 사용해 카카오 사용자 정보 조회 API를 호출하기 위한 헤더 설정
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.setBearerAuth(kakaoToken.getAccessToken());
        HttpEntity<Void> profileEntity = new HttpEntity<>(profileHeaders);

        KakaoUserResponse kakaoUser = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileEntity,
                KakaoUserResponse.class
        ).getBody();

        String kakaoId = kakaoUser.getId().toString();
        // 카카오 사용자 ID를 기반으로 우리 서비스 DB에서 기존 가입 여부를 확인한다. 신규 가입 여부 판단에 사용된다.
        User existing = userMapper.findByKakaoId(kakaoId);

        // 기존 유저가 없는 경우, 신규 유저로 판단하여 추가 정보 입력을 유도하는 KakaoLoginResponse를 생성한다.
        if (existing == null) {
            return KakaoLoginResponse.builder()
                    .isNewUser(true)
                    .email(kakaoUser.getKakaoAccount().getEmail())
                    .nickname(kakaoUser.getKakaoAccount().getProfile().getNickname())
                    .name(kakaoUser.getKakaoAccount().getProfile().getNickname())
                    .kakaoId(kakaoId)
                    .phoneNumber("")
                    .gender("")
                    .build();
        } else {
            // 기존 유저일 경우, JWT 토큰을 발급하여 바로 로그인 처리를 위한 KakaoLoginResponse를 생성합니다.
            String at = jwtProvider.createAccessToken(existing.getUserId(), existing.getRole().name());
            String rt = jwtProvider.createRefreshToken(existing.getUserId(), existing.getRole().name());
            return KakaoLoginResponse.builder()
                    .isNewUser(false)
                    .accessToken(at)
                    .refreshToken(rt)
                    .build();
        }
    }
}
