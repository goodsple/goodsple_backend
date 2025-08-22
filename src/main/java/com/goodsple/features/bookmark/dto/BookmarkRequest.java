package com.goodsple.features.bookmark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkRequest {

    private Long folderId;
    private Long postId;
    private String postType;    // exchange | event
}
