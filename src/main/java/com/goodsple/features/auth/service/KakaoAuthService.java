package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.request.KakaoSignUpRequest;
import com.goodsple.features.auth.dto.response.KakaoLoginResponse;
import com.goodsple.features.auth.dto.response.KakaoTokenResponse;
import com.goodsple.features.auth.dto.response.KakaoUserResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.LoginType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Optional;

/**
 * Kakao OAuth2 로그인 및 회원가입 로직을 처리하는 서비스 클래스
 */
@RequiredArgsConstructor
@Service
public class KakaoAuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtProvider;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

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

    // --- 내부 헬퍼 메서드 -----------------------------------------------

    // 1) 인가코드(code)를 카카오 서버에 전달하여 액세스 토큰을 발급받는다.
    private KakaoTokenResponse getToken(String code) {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type",    "authorization_code");
        tokenRequest.add("client_id",     clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("redirect_uri",  redirectUri);
        tokenRequest.add("code",          code);

        HttpEntity<MultiValueMap<String, String>> tokenEntity =
                new HttpEntity<>(tokenRequest, tokenHeaders);

        return restTemplate.exchange(
                        "https://kauth.kakao.com/oauth/token",
                        HttpMethod.POST,
                        tokenEntity,
                        KakaoTokenResponse.class)
                .getBody();
    }

    // 2) 발급받은 액세스 토큰을 이용해 사용자 프로필 정보를 조회한다.
    private KakaoUserResponse getProfile(KakaoTokenResponse kakaoToken) {
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.setBearerAuth(kakaoToken.getAccessToken());
        HttpEntity<Void> profileEntity = new HttpEntity<>(profileHeaders);

        return restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.GET,
                        profileEntity,
                        KakaoUserResponse.class)
                .getBody();
    }

    /**
     * 카카오 로그인 콜백에서 호출된다.
     * code 만으로 신규/기존 판단하여 기본 정보 반환
     */
    public KakaoLoginResponse loginWithKakao(String code) {
        // 1) 토큰 발급
        KakaoTokenResponse token = getToken(code);
        // 2) 프로필 조회
        KakaoUserResponse profile = getProfile(token);

        String kakaoId = profile.getId().toString();
        String email = Optional.ofNullable(profile.getKakaoAccount())
                .map(a -> a.getEmail()).orElse("");
        String nickname = Optional.ofNullable(profile.getKakaoAccount())
                .map(a -> a.getProfile())
                .map(p -> p.getNickname()).orElse("");

        // 3) 기존 사용자 여부 확인
        User existing = userMapper.findByKakaoId(kakaoId);
        if (existing == null) {
            // 신규 사용자: 기본 정보만 응답
            return KakaoLoginResponse.builder()
                    .isNewUser(true)
                    .email(email)
                    .nickname(nickname)
                    .kakaoId(kakaoId)
                    .build();
        }
        // 기존 사용자: JWT 토큰 발급
        String at = jwtProvider.createAccessToken(existing.getUserId(), existing.getRole().name());
        String rt = jwtProvider.createRefreshToken(existing.getUserId(), existing.getRole().name());
        return KakaoLoginResponse.builder()
                .isNewUser(false)
                .accessToken(at)
                .refreshToken(rt)
                .build();
    }

    /**
     * 로그인 후 추가정보(req)를 받아 회원정보를 업데이트하거나 신규가입 처리
     */
    public KakaoLoginResponse loginWithKakao(String code, KakaoSignUpRequest req) {
        // 기본 loginWithKakao 흐름 재사용
        KakaoLoginResponse resp = loginWithKakao(code);
        if (!resp.isNewUser()) return resp; // 기존 유저면 그대로 반환

        // 신규 사용자에 대해 추가정보를 저장하고, 업데이트된 응답 반환
        User user = User.builder()
                .loginId(resp.getKakaoId())
                .email(resp.getEmail())
                .nickname(req.getNickname())
                .name(req.getNickname())
                .phoneNumber(req.getPhoneNumber())
                .gender(req.getGender())
                .birthDate(req.getBirthDate())
                .role(Role.USER)
                .loginType(LoginType.KAKAO)
                .kakaoId(resp.getKakaoId())
                .build();
        userMapper.createUser(user);

        return KakaoLoginResponse.builder()
                .isNewUser(false)
                .accessToken(jwtProvider.createAccessToken(user.getUserId(), Role.USER.name()))
                .refreshToken(jwtProvider.createRefreshToken(user.getUserId(), Role.USER.name()))
                .build();
    }

    // --- 회원가입 처리 메서드 ------------------------------------------

    /**
     * 프론트엔드에서 추가 정보를 입력받아 최종 회원가입을 수행합니다.
     * @param req 전화번호, 성별, 생년월일 등을 포함한 DTO
     * @return 생성된 사용자로 발급된 JWT 토큰
     */

    public TokenResponse signUpWithKakao(KakaoSignUpRequest req) {
        // 1) User 엔티티 생성 및 저장
        User user = User.builder()
                .loginId(req.getKakaoId())
                .nickname(req.getNickname())
                .email(req.getEmail())
                .name(req.getNickname())
                .phoneNumber(req.getPhoneNumber())
                .gender(req.getGender())
                .birthDate(req.getBirthDate())
                .role(Role.USER)
                .loginType(LoginType.KAKAO)
                .kakaoId(req.getKakaoId())
                .build();
        userMapper.createUser(user);
        Long userId = userMapper.findByKakaoId(req.getKakaoId()).getUserId();

        // 2) JWT 토큰 발급
        String accessToken  = jwtProvider.createAccessToken(userId, Role.USER.name());
        String refreshToken = jwtProvider.createRefreshToken(userId, Role.USER.name());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

