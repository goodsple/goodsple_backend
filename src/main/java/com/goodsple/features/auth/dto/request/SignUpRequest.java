package com.goodsple.features.auth.dto.request;

import com.goodsple.features.auth.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "회원가입 요청 정보")
public class SignUpRequest {

    @Schema(description = "로그인 아이디", example = "testuser123")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$\", message = \"영문 + 숫자 조합으로 5~20자 이내로 입력해주세요.")
    private String loginId;

    @Schema(description = "비밀번호", example = "password123")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$\", message = \"영문, 숫자, 특수문자를 포함해 8자 이상 입력해주세요.")
    private String password;

    private String passwordCheck; // 프론트에서 비밀번호 확인용

    @Schema(description = "닉네임", example = "둥둥이")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,20}$\", message = \"닉네임에는 특수문자를 사용할 수 없습니다.")
    private String nickname;

    @Schema(description = "이름", example = "홍길동")
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$\", message = \"이름은 한글 또는 영문으로 입력해주세요.")
    private String name;

    @Schema(description = "이메일", example = "test@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "휴대폰 번호", example = "01012345678")
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^010\\\\d{7,8}$\", message = \"숫자만 입력해주세요. ex: 01012345678")
    private String phoneNumber;

    @Schema(description = "생년월일", example = "1990-01-01")
    @NotNull(message = "생년월일을 선택해주세요.")
    private LocalDate birthDate;

    @Schema(description = "성별", example = "male")
    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

}
