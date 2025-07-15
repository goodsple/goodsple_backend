package com.goodsple.features.auth.dto.request;

import com.goodsple.features.auth.enums.CheckType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckDuplicateRequest {
    // 체크 요청을 보낼 때 필요한 정보만 담는 DTO
    // 아이디, 닉네임, 이메일, 휴대폰번호 보내서 checktype으로 구분
    private CheckType checkType;
    private String value;
}
