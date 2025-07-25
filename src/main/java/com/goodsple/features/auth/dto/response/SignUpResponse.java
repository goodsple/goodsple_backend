package com.goodsple.features.auth.dto.response;

import com.goodsple.features.auth.enums.Gender;
import com.goodsple.features.auth.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 완료 후 사용자 정보 응답")
public class SignUpResponse {
    // 핸드폰번호, 비밀번호, 생년월일 같은 개인정보는 응답에 제외
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "로그인 아이디", example = "testuser123")
    private String loginId;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "닉네임", example = "둥둥이")
    private String nickname;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "권한", example = "USER")
    private Role role;
}
