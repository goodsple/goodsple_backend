package com.goodsple.features.auth.entity;

import com.goodsple.features.auth.enums.Gender;
import com.goodsple.features.auth.enums.LoginType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Data
@Builder
public class User {
    private Long userId;
    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;

    @Builder.Default
    private Role role = Role.USER;
    private String profileImage;

    @Builder.Default
    private SuspensionStatus suspensionStatus = SuspensionStatus.ACTIVE;
    private OffsetDateTime suspendedUntil;
    private OffsetDateTime userCreatedAt;
    private OffsetDateTime userUpdatedAt;

    @Builder.Default
    private LoginType loginType = LoginType.LOCAL;
    private String kakaoId;
    @Builder.Default
    private Boolean isBannedFromAuction = false;
}
