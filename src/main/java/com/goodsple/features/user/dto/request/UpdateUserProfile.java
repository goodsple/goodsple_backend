package com.goodsple.features.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "내 프로필 수정 요청")
public class UpdateUserProfile {

    @Schema(description = "프로필 이미지 URL", example = "https://…/profile.jpg")
    private String profileImageUrl;   // 보내면 수정, 안 보내면 무시

    @Schema(description = "이름", example = "홍길동")
    private String name;              // 변경 없으면 null

    @Schema(description = "닉네임", example = "둥둥이")
    private String nickname;          // 변경 없으면 null

    @Pattern(
            regexp = "^$|^\\d{2,3}-?\\d{3,4}-?\\d{4}$",
            message = "올바른 휴대폰 번호여야 합니다"
    )
    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String phoneNumber;       // 변경 없으면 null

    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Schema(description = "이메일 (LOCAL 로그인만 변경 가능)", example = "test@example.com")
    private String email;             // LOCAL 로그인인 경우에만 반영

    private String currentPassword; // 비밀번호 변경 시 필수

    @Schema(description = "비밀번호 (선택 변경)", example = "새비밀번호123!")
    private String newPassword;

}
