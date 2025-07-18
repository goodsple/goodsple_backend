package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.RefreshRequest;
import com.goodsple.features.auth.dto.response.KakaoLoginResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.service.KakaoAuthService;
import com.goodsple.features.auth.service.UserService;
import com.goodsple.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Tag(name = "Auth", description = "로그인 및 토큰 재발급 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtProvider;
    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "로그인", description = "사용자 로그인 후 액세스 토큰과 리프레시 토큰을 반환합니다.")
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
    @Operation(summary = "카카오 로그인 URL 반환", description = "카카오 OAuth2 인증용 URL을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestParam("code") String code) {
        KakaoLoginResponse response = kakaoAuthService.loginWithKakao(code);
        return ResponseEntity.ok(response);
    }
}
