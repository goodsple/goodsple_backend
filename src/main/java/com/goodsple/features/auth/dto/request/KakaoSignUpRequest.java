package com.goodsple.features.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goodsple.features.auth.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카카오 추가정보 요청 정보")
public class KakaoSignUpRequest {

    private String email;
    @Schema(description = "닉네임", example = "둥둥이")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,12}$", message = "닉네임은 2~12자 이내로 입력해주세요. 특수문자는 사용할 수 없습니다.")
    private String nickname;

    private String kakaoId;

    @Schema(description = "휴대폰 번호", example = "01012345678")
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^010\\d{7,8}$", message = "숫자만 입력해주세요. ex: 01012345678")
    private String phoneNumber;

    @Schema(description = "성별", example = "MALE")
    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @Schema(description = "생년월일", example = "1990-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생년월일을 선택해주세요.")
    private LocalDate birthDate;


}
