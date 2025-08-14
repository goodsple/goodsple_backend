package com.goodsple.features.admin.user.controller;

import com.goodsple.features.admin.user.dto.AdminUpdateUserRequest;
import com.goodsple.features.admin.user.dto.AdminUserDetail;
import com.goodsple.features.admin.user.dto.AdminUserSearchCond;
import com.goodsple.features.admin.user.dto.AdminUserSummary;
import com.goodsple.features.admin.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin Users", description = "관리자 회원 관리 API")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Operation(summary = "회원 목록 조회", description = "검색/필터/페이징 적용된 회원 목록 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    @GetMapping
    // 스프링 시큐리티의 메서드 보안 어노테이션, 해당 메서드를 호출하기 전에(Pre) 권한을 검사해서, ADMIN 역할이 있는 사용자만 실행할 수 있게 막는다
    @PreAuthorize("hasRole('ADMIN')") // 또는 hasAuthority('ADMIN')
    public List<AdminUserSummary> list(@ModelAttribute AdminUserSearchCond cond) {
        return adminUserService.list(cond);
    }

    @Operation(summary = "회원 총 개수", description = "동일한 검색/필터 조건 기준의 총 개수")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public long count(@ModelAttribute AdminUserSearchCond cond) {
        return adminUserService.count(cond);
    }

    @Operation(summary = "회원 상세 조회", description = "목록에서 행 클릭 시 상세 모달 데이터 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @ApiResponse(responseCode = "404", description = "대상 없음")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDetail detail(@PathVariable Long userId) {
        return adminUserService.detail(userId);
    }

    @Operation(summary = "회원 상태/권한 변경", description = "status/role 중 전달된 값만 변경 (둘 다 nullable)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>  update(@PathVariable Long userId,
                       @RequestBody AdminUpdateUserRequest req) {
        adminUserService.update(userId, req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 하드 삭제", description = "탈퇴(withdrawn) 상태에서만 실제 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @ApiResponse(responseCode = "409", description = "탈퇴 상태가 아님")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void hardDelete(@PathVariable Long userId) {
        adminUserService.hardDelete(userId);
    }
}
