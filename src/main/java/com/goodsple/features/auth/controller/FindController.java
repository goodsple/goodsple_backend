package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.*;
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
@Tag(name = "Find", description = "아이디 찾기 및 비밀번호 찾기 API")
public class FindController {

    private final UserService userService;

    // 아이디 찾기 인증번호 요청
    @Operation(summary = "아이디 찾기 인증번호 발급", description = "사용자가 이메일을 입력하면 인증번호를 발급하여 이메일로 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호가 이메일로 성공적으로 전송됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 주소"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/find-id/request")
    public ResponseEntity<String> requestFindIdCode(@RequestBody @Valid FindIdCodeRequest findIdCode) {
        userService.requestFindIdCode(findIdCode.getEmail());
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    // 아이디 찾기: 인증번호 검증 후 아이디 반환
    @Operation(summary = "아이디 찾기", description = "인증번호를 검증하고, 유효한 경우 이메일로 해당 아이디를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원님의 아이디 반환"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 인증번호"),
            @ApiResponse(responseCode = "404", description = "일치하는 회원이 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/find-id")
    public ResponseEntity<String> findLoginId(@RequestBody @Valid FindIdRequest findIdRequest) {
        String loginId = userService.findLoginId(
                findIdRequest.getName(), findIdRequest.getEmail(), findIdRequest.getCode());
        return ResponseEntity.ok("loginId: " + loginId);
    }

    // 비밀번호 찾기 - 인증번호 요청
    @Operation(summary = "비밀번호 재설정 인증번호 발급", description = "사용자가 아이디와 이메일을 입력하면 인증번호를 이메일로 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 회원 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/find-password/request")
    public ResponseEntity<String> requestResetPasswordCode(@RequestBody @Valid ResetPasswordRequest request) {
        userService.requestResetPasswordCode(request.getLoginId(), request.getEmail());
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    // 비밀번호 찾기 - 인증번호 검증
    @Operation(summary = "비밀번호 재설정 인증번호 검증", description = "사용자가 입력한 인증번호를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 일치"),
            @ApiResponse(responseCode = "400", description = "인증번호 불일치 또는 시간 초과"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/find-password/verify")
    public ResponseEntity<String> verifyResetPasswordCode(@RequestBody @Valid VerifyCode request) {
        userService.verifyResetPasswordCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok("인증번호 검증 성공");
    }

    // 비밀번호 재설정
    @Operation(summary = "비밀번호 재설정", description = "인증 완료된 사용자가 새 비밀번호를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/find-password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPw request) {
        userService.resetPassword(request.getLoginId(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
