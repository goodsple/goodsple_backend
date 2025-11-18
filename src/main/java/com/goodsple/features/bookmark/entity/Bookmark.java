package com.goodsple.features.bookmark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {
    private Long bookmarkId;
    private Long userId;
    private Long folderId;
    private Long exchangePostId;
    private Long eventPostId;
    private OffsetDateTime bookmarkedAt;
}
