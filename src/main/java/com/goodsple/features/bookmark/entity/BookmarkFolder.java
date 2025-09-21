package com.goodsple.features.bookmark.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkFolder {
    private Long folderId;
    private String folderName;
    private String folderColor;
    private Long userId;
}
