package com.goodsple.features.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPostResponse {
    private Long exchangePostId;
    private String exchangePostTitle;
    private String postLocationName;
    private String postHopeRegion;
    private String postTradeType; // DIRECT, DELIVERY, BOTH
    private OffsetDateTime exchangePostCreatedAt;
    private String postTradeStatus; // AVAILABLE, ONGOING, COMPLETED
    private Long userId;
    private String postImageUrl;
}
