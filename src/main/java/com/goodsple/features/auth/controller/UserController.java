package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "회원가입 및 인증 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 요청을 처리합니다.")
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

}
