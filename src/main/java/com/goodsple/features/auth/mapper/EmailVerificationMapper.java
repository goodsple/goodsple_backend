package com.goodsple.features.auth.mapper;

import com.goodsple.features.auth.dto.response.EmailVerificationResponse;
import com.goodsple.features.auth.entity.EmailVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface EmailVerificationMapper {

    /** 발급된 인증코드를 저장 */
    void insert(EmailVerification verification);

    /** 인증번호 검증 (used=false, 만료시간 이후 제외) */
    Optional<EmailVerificationResponse> findByEmailAndCode(
            @Param("email") String email,
            @Param("code")  String code
    );

    /** 인증코드를 사용 처리(used=true) */
    void updateUsed(
            @Param("email") String email,
            @Param("code")  String code
    );

    /** (선택) 최신 발급 레코드만 보고 싶을 때 */
    EmailVerification getLatestByEmail(@Param("email") String email);
}
