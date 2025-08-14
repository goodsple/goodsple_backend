package com.goodsple.features.admin.user.dto;

import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "관리자 회원 상태/권한 변경 요청 DTO")
public class AdminUpdateUserRequest {

    @Schema(description = "활동 상태", example = "suspended", allowableValues = {"active","suspended","withdrawn"})
    private SuspensionStatus status;

    @Schema(description = "권한", example = "admin", allowableValues = {"user","admin"})
    private Role role;
}
