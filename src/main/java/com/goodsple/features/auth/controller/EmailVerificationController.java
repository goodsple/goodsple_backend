package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.EmailVerificationRequest;
import com.goodsple.features.auth.dto.response.EmailVerificationResponse;
import com.goodsple.features.auth.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "EmailVerification", description = "이메일 인증 코드 발급 및 검증 API")
public class EmailVerificationController {

   private final EmailVerificationService emailVerificationService;

    // 이메일 인증 코드 검증
    @Operation(summary = "이메일 인증 코드 검증", description = "입력된 이메일과 인증 코드를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 코드 또는 만료된 인증 코드"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/verify-code")
    public ResponseEntity<EmailVerificationResponse> verifyCode(@RequestBody @Valid EmailVerificationRequest request) {
        try {
            // 인증 코드 검증
            EmailVerificationResponse response = emailVerificationService.verifyCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok(response);  // 인증이 완료된 인증 정보를 반환
        } catch (IllegalArgumentException e) {
            // 오류 발생 시
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
