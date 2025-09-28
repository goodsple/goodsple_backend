package com.goodsple.features.auth.entity;

import com.goodsple.features.auth.enums.Gender;
import com.goodsple.features.auth.enums.LoginType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
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

    private Role role = Role.USER;
    private String profileImage;

    private SuspensionStatus suspensionStatus = SuspensionStatus.ACTIVE;
    private OffsetDateTime suspendedUntil;
    private OffsetDateTime userCreatedAt;
    private OffsetDateTime userUpdatedAt;

    private LoginType loginType = LoginType.LOCAL;

    /** MyBatis 매퍼에서 이 값을 사용하도록 추가 */
    public String getLoginTypeLower() {
        return loginType.name().toLowerCase();  // "LOCAL" → "local"
    }

    private String kakaoId;
    private Boolean isBannedFromAuction = false;
    private OffsetDateTime auctionBanUntil;

    @Builder
    private User(
            Long userId,
            String loginId,
            String password,
            String name,
            String nickname,
            String email,
            String phoneNumber,
            LocalDate birthDate,
            Gender gender,
            Role role,
            String profileImage,
            SuspensionStatus suspensionStatus,
            OffsetDateTime suspendedUntil,
            OffsetDateTime userCreatedAt,
            OffsetDateTime userUpdatedAt,
            LoginType loginType,
            String kakaoId,
            Boolean isBannedFromAuction,
            OffsetDateTime auctionBanUntil // [추가]
    ) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role                = role                != null ? role                : Role.USER;
        this.profileImage        = profileImage;
        this.suspensionStatus    = suspensionStatus    != null ? suspensionStatus    : SuspensionStatus.ACTIVE;
        this.suspendedUntil      = suspendedUntil;
        this.userCreatedAt       = userCreatedAt;
        this.userUpdatedAt       = userUpdatedAt;
        this.loginType           = loginType           != null ? loginType           : LoginType.LOCAL;
        this.kakaoId             = kakaoId;
        this.isBannedFromAuction = isBannedFromAuction != null ? isBannedFromAuction : Boolean.FALSE;
        this.auctionBanUntil = auctionBanUntil; // [추가]
    }
}
