package com.goodsple.features.bookmark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class MoveBookmarkRequest {
    private List<Long> bookmarkIds;   // 이동할 북마크 ID들
    private Long targetFolderId;      // 목표 폴더 ID
}