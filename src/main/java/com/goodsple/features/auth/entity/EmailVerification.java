package com.goodsple.features.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * email_verification 테이블의 한 레코드를 매핑하는 엔티티
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {
    private Long verificationId;
    private String email;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;
}
