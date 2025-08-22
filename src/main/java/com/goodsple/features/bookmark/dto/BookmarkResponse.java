package com.goodsple.features.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookmarkResponse {
    private Long bookmarkId;
    private Long folderId;
    private Long postId;
    private String postType;    // "exchange" | "event"
    private LocalDateTime bookmarkedAt;
}
