package com.goodsple.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 재설정")
public class ResetPw {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
            message = "영문, 숫자, 특수문자를 포함해 8자 이상 입력해주세요.")
    private String newPassword;
}
