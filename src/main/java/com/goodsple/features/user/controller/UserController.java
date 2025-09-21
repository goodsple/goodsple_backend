package com.goodsple.features.user.controller;

import com.goodsple.features.user.dto.request.UpdateUserProfile;
import com.goodsple.features.user.dto.request.UserInfo;
import com.goodsple.features.user.dto.response.UserProfile;
import com.goodsple.features.auth.service.UserService;
import com.goodsple.security.CustomUserDetails;
import com.goodsple.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "로그인 사용자 프로필 관리 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 기본 정보 조회", description = "마이페이지 헤더 등에서 사용할 닉네임·이미지 URL만 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getMyInfo(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        UserInfo info = userService.getUserInfo(userId);
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "내 상세 프로필 조회", description = "이름, 이메일 등 전체 프로필 정보를 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
    })
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfile> getMyProfile(Authentication authentication) {
        // Authentication에서 principal(Object) 꺼내기
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getUserId();

        UserProfile profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "내 프로필 수정", description = "로그인된 유저의 프로필 정보를 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfile req
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        userService.updateMyProfile(user.getUserId(), req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴", description = "로그인된 유저의 계정을 탈퇴 처리(소프트 삭제)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdrawMyAccount(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        userService.withdrawMyAccount(user.getUserId());
        return ResponseEntity.noContent().build();
    }
}
