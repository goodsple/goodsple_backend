package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.response.EmailVerificationResponse;
import com.goodsple.features.auth.entity.EmailVerification;
import com.goodsple.features.auth.mapper.EmailVerificationMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

// 인증 코드 발송
@Service
public class EmailVerificationService {


    private final StringRedisTemplate redis;
    private final long expirationSeconds;

    @Autowired
    public EmailVerificationService(StringRedisTemplate redis,
                                    @Value("${email.verification.expiration-time}") long minutes) {
        this.redis = redis;
        this.expirationSeconds = minutes * 60;
    }

    // purpose 분리된 키로 코드 생성·저장
    public String createAndSaveCode(String email, String purpose) {
        String code = RandomStringUtils.randomNumeric(6);
        String key  = "email:verification:" + purpose + ":" + email;
        redis.opsForValue().set(key, code, expirationSeconds, TimeUnit.SECONDS);
        return code;
    }

    // purpose 분리된 키로 코드 검증·삭제
    public void verifyCode(String email, String code, String purpose) {
        String key   = "email:verification:" + purpose + ":" + email;
        String saved = redis.opsForValue().get(key);
        if (saved == null) {
            throw new IllegalArgumentException("인증 코드가 없거나 만료되었습니다.");
        }
        if (!saved.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");
        }
        redis.delete(key);
    }
}
