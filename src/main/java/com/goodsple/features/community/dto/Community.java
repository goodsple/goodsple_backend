package com.goodsple.features.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class Community {

    private Long communityId;
    private Long userId;
    private String commRoomId;       // K-POP | MOVIE | GAME | ANIMATION
    private String type;             // GENERAL | MEGAPHONE
    private String content;
    private String commCreatedAt;

    private String nickname;
    private String userProfile;

    private String badgeImageUrl;
    private String badgeName;
    private String badgeLevel;
}
