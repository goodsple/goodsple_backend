package com.goodsple.features.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkInfoResponse {
    private int bookmarkCount;
    private boolean isBookmarked;
}