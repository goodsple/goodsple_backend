package com.goodsple.features.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class Community {

    private Long communityId;
    private Long userId;
    private String roomId;       // K-POP | MOVIE | GAME | ANIMATION
    private String content;
    private String createdAt;
    private String userName;
    private String userProfile;
}
