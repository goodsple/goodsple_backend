package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.response.EmailVerificationResponse;
import com.goodsple.features.auth.mapper.EmailVerificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// 인증 코드 발송
@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationMapper emailVerificationMapper;

    // 인증 코드 검증 메서드
    public EmailVerificationResponse verifyCode(String email, String code) {
        // 이메일과 인증 코드로 인증 정보를 조회
        EmailVerificationResponse verification = emailVerificationMapper.findByEmailAndCode(email, code)
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 유효하지 않거나 존재하지 않습니다."));

        // 인증 코드가 만료되었는지 체크
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("인증 코드의 유효 기간이 만료되었습니다.");
        }

        // 인증 코드 사용 처리 (이미 사용된 코드라면, 재사용 불가)
        if (verification.isUsed()) {
            throw new IllegalArgumentException("이미 사용된 인증 코드입니다.");
        }

        // 인증 코드 사용 처리 (중복 사용 방지)
        emailVerificationMapper.updateUsed(email, code);  // 사용 처리

        return verification;  // 인증이 완료되면, 이메일 인증 정보를 반환
    }
}
