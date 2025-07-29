package com.goodsple.features.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String nickname;
    private String profileImageUrl;
}
