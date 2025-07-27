package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.KakaoSignUpRequest;
import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.RefreshRequest;
import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.KakaoLoginResponse;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.service.KakaoAuthService;
import com.goodsple.features.auth.service.UserService;
import com.goodsple.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Tag(
        name = "Auth",
        description = "로컬/카카오 로그인·회원가입·토큰 재발급 API"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // 프론트 주소
    private static final String FRONT_BASE = "http://localhost:5173";

    private final UserService userService;
    private final JwtTokenProvider jwtProvider;
    private final KakaoAuthService kakaoAuthService;

    // ====================================================================================
    // 1. 로컬 회원가입 & 로그인
    // ====================================================================================

    @Operation(summary = "로컬 회원가입", description = "로컬 회원가입 요청을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = userService.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "중복 확인", description = "아이디, 닉네임, 이메일, 휴대폰 번호 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 여부 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check")
    public  ResponseEntity<Map<String,Boolean>>checkDuplicate(
            @RequestParam("type") CheckType checkType,
            @RequestParam("value") String value) {
        boolean available = userService.isAvailable(checkType, value);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", available);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "로컬 로그인", description = "사용자 로그인 후 액세스 토큰과 리프레시 토큰을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        // 아이디+비밀번호 검증 및 토큰 생성 로직을 UserService에서 처리
        TokenResponse tokens = userService.login(loginRequest);
        return ResponseEntity.ok(tokens);
    }

    // ====================================================================================
    // 2. 토큰 재발급
    // ====================================================================================

    @Operation(summary = "토큰 재발급", description = "유효한 리프레시 토큰으로 새로운 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        // 1) 토큰 유효성 검사
        if (!jwtProvider.validateToken(request.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }
        // 2) 토큰에서 사용자 정보 추출
        Long userId = jwtProvider.getUserId(request.getRefreshToken());
        String role = jwtProvider.getRole(request.getRefreshToken());
        // 3) 새로운 액세스 토큰 발급
        String newAccessToken = jwtProvider.createAccessToken(userId, role);
        // 4) 응답
        return ResponseEntity.ok(
                TokenResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(request.getRefreshToken())  // 리프레시 토큰은 그대로 돌려줌
                        .build()
        );
    }

    // ====================================================================================
    // 3. 카카오 OAuth2 로그인 & 회원가입
    // ====================================================================================

    @Operation(summary = "카카오 로그인 URL 반환", description = "카카오 OAuth2 인증용 URL을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    /* 클라이언트에게 카카오 인가 URL을 돌려준다. */
    @GetMapping("/kakao/url")
    public ResponseEntity<Map<String, String>> getKakaoUrl() {
        String url = kakaoAuthService.getAuthorizationUrl();
        return ResponseEntity.ok(Map.of("url", url));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 콜백 코드로 로그인 처리 후 JWT 또는 신규 가입 여부를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공 또는 신규 가입 필요"),
            @ApiResponse(responseCode = "401", description = "카카오 인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    /* 카카오가 이 콜백을 호출한다. */
    @GetMapping("/kakao/callback")
    public void kakaoCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {

        // KakaoLoginResponse 객체를 가져옴 (카카오 로그인 후 받은 응답)
        KakaoLoginResponse resp = kakaoAuthService.loginWithKakao(code);

        if (resp.isNewUser()) {
            // 신규 사용자 → 프론트 회원가입 페이지로 리다이렉트 (query string 으로 기본 정보 전달)
            String params = String.format(
                    "?email=%s&nickname=%s&kakaoId=%s",
                    URLEncoder.encode(resp.getEmail(),    "UTF-8"),
                    URLEncoder.encode(resp.getNickname(), "UTF-8"),
                    URLEncoder.encode(resp.getKakaoId(),   "UTF-8")
            );
            // 회원가입 페이지로 리다이렉트
            response.sendRedirect(FRONT_BASE + "/signup/kakao" + params);

        } else {
            // 기존 사용자 → 토큰을 쿼리 파라미터로 카카오 콜백 전용 프론트 경로로 리다이렉트
            String redirectUrl = String.format(
                    FRONT_BASE + "/kakao/callback?at=%s&rt=%s",
                    resp.getAccessToken(),
                    resp.getRefreshToken()
            );
            response.sendRedirect(redirectUrl);
        }
    }

    @Operation(summary = "카카오 회원가입", description = "카카오 로그인 후 추가 정보를 받아 최종 회원가입을 수행하고 JWT를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 및 토큰 발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(필수 정보 누락 등)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup/kakao")
    public ResponseEntity<TokenResponse> signUpWithKakao(
            @Valid @RequestBody KakaoSignUpRequest req) {
        TokenResponse tokens = kakaoAuthService.signUpWithKakao(req);
        return ResponseEntity.ok(tokens);
    }
}
