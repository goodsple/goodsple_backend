package com.goodsple.features.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodsple.features.auth.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인된 사용자 프로필 정보")
public class UserProfile {

    @Schema(description = "로그인 아이디", example = "testuser123")
    private String loginId;

    @Schema(description = "닉네임", example = "둥둥이")
    private String nickname;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "생년월일", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "프로필 이미지 URL (선택)", example = "https://…/profile.jpg")
    private String profileImageUrl;
}
