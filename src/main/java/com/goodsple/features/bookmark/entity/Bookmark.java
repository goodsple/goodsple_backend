package com.goodsple.features.bookmark.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Bookmark {
    private Long bookmarkId;
    private Long userId;
    private Long folderId;
    private Long exchangePostId;
    private Long eventPostId;
    private LocalDateTime bookmarkedAt;
}
