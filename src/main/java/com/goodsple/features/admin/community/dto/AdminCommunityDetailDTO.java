package com.goodsple.features.admin.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommunityDetailDTO {
    private Long communityId;
    private Long userId;
    private String commRoomId;
    private String content;
    private OffsetDateTime commCreatedAt;
    private String type;
}
