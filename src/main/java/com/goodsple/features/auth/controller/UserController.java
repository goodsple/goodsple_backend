package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Signup", description = "로컬 회원가입 및 인증 관련 API")
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
}
