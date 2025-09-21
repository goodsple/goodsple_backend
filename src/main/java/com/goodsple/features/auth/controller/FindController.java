package com.goodsple.features.auth.controller;

import com.goodsple.features.auth.dto.request.*;
import com.goodsple.features.auth.service.EmailService;
import com.goodsple.features.auth.service.EmailVerificationService;
import com.goodsple.features.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Find", description = "아이디 찾기 및 비밀번호 찾기 API")
@Slf4j
public class FindController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;

    // 아이디 찾기 인증번호 요청
    @Operation(summary = "아이디 찾기 인증번호 발급", description = "사용자가 이메일을 입력하면 인증번호를 발급하여 이메일로 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "인증번호가 이메일로 성공적으로 전송됨"),
            @ApiResponse(responseCode = "404", description = "일치하는 회원 없음")
    })
    @PostMapping("/find-id/request")
    public ResponseEntity<Void> requestFindIdCode(@RequestBody @Valid FindIdCodeRequest req) {
        log.debug(">> find-id/request called: name={}, email={}", req.getName(), req.getEmail());

        // 1) 회원 존재 여부 (이름+이메일)
        if (!userService.existsByNameAndEmail(req.getName(), req.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2) Redis에 코드 저장 (TTL 자동 관리)
//        String code = emailVerificationService.createAndSaveCode(req.getEmail());
        String code = emailVerificationService.createAndSaveCode(req.getEmail(),"find-id");

        // 3) 메일 전송(purpose="find-id")
        emailService.sendVerificationEmail(
                req.getEmail(),
                code,
                "find-id"
        );

        // 4) 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
        try {
            // ✅ 서비스에 검증+조회 로직 모두 위임
            String loginId = userService.findLoginId(
                    findIdRequest.getName(),
                    findIdRequest.getEmail(),
                    findIdRequest.getCode()
            );
            return ResponseEntity.ok(loginId);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 비밀번호 찾기 - 인증번호 요청
    @Operation(summary = "비밀번호 재설정 인증번호 발급", description = "사용자가 아이디와 이메일을 입력하면 인증번호를 이메일로 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 회원 없음")
    })
    @PostMapping("/find-password/request")
    public ResponseEntity<Void> requestResetPasswordCode(@RequestBody @Valid ResetPasswordRequest req) {
        // 1) 회원 존재 여부 (로그인ID+이메일)
        if (!userService.existsByLoginIdAndEmail(req.getLoginId(), req.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2) Redis에 코드 저장
        String code = emailVerificationService.createAndSaveCode(req.getEmail(),"find-password");

        // 3) 메일 전송(purpose="reset-password")
        emailService.sendVerificationEmail(
                req.getEmail(),
                code,
                "reset-password"
        );

        // 4) 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 비밀번호 찾기 - 인증번호 검증
    @Operation(summary = "비밀번호 재설정 인증번호 검증", description = "사용자가 입력한 인증번호를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 일치"),
            @ApiResponse(responseCode = "400", description = "인증번호 불일치 또는 시간 초과")
    })
    @PostMapping("/find-password/verify")
    public ResponseEntity<Void> verifyResetPasswordCode(@RequestBody @Valid VerifyCode req) {
        try {
            emailVerificationService.verifyCode(req.getEmail(), req.getCode(), "find-password");
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 비밀번호 재설정
    @Operation(summary = "비밀번호 재설정", description = "인증 완료된 사용자가 새 비밀번호를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @PostMapping("/find-password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPw request) {
        userService.resetPassword(request.getLoginId(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
