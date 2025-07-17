package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.service.KakaoAuthService;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final JwtTokenProvider jwtProvider;
    private final KakaoAuthService kakaoAuthService;


}
