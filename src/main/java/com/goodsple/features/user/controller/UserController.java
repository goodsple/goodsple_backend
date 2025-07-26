package com.goodsple.features.user.controller;

import com.goodsple.features.user.dto.request.UpdateUserProfile;
import com.goodsple.features.user.dto.response.UserProfile;
import com.goodsple.features.auth.service.UserService;
import com.goodsple.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "로그인 사용자 프로필 관리 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtProvider;


    @Operation(summary = "내 프로필 조회", description = "로그인된 유저의 프로필 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(
            @AuthenticationPrincipal(expression = "userId") Long userId
    ) {
        // Service 호출: DB에서 사용자 프로필 조회 후 DTO 변환
        UserProfile profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "내 프로필 수정", description = "로그인된 유저의 프로필 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @Valid @RequestBody UpdateUserProfile req
    ) {
        // Service 호출: 변경 요청이 있는 필드만 업데이트
        userService.updateMyProfile(userId, req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴", description = "로그인된 유저의 계정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(
            @AuthenticationPrincipal(expression = "userId") Long userId
    ) {
        // Service 호출: DB에서 사용자 레코드 삭제
        userService.deleteMyProfile(userId);
        return ResponseEntity.noContent().build();
    }
}
