package com.goodsple.features.bookmark.service;

import com.goodsple.features.bookmark.dto.BookmarkRequest;
import com.goodsple.features.bookmark.dto.BookmarkResponse;
import com.goodsple.features.bookmark.entity.Bookmark;
import com.goodsple.features.bookmark.mapper.BookmarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    @Transactional
    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request) {
        Long exchangePostId = "exchange".equals(request.getPostType()) ? request.getPostId() : null;
        Long eventPostId = "event".equals(request.getPostType()) ? request.getPostId() : null;

        boolean exists = bookmarkMapper.exists(userId, exchangePostId, eventPostId);
        if (exists) {
            throw new IllegalArgumentException("이미 북마크된 게시글입니다.");
        }

        bookmarkMapper.save(userId, request.getFolderId(), exchangePostId, eventPostId);

        List<Bookmark> bookmarks = bookmarkMapper.findByUserId(userId);
        Bookmark latest = bookmarks.get(0);

        Long postId = exchangePostId != null ? exchangePostId : eventPostId;
        return new BookmarkResponse(
                latest.getBookmarkId(),
                latest.getFolderId(),
                postId,
                request.getPostType(),
                latest.getBookmarkedAt()
        );
    }

    public List<BookmarkResponse> getUserBookmarks(Long userId) {
        List<Bookmark> bookmarks = bookmarkMapper.findByUserId(userId);
        return bookmarks.stream().map(b -> {
            String postType = b.getExchangePostId() != null ? "exchange" : "event";
            Long postId = b.getExchangePostId() != null ? b.getExchangePostId() : b.getEventPostId();
            return new BookmarkResponse(
                    b.getBookmarkId(),
                    b.getFolderId(),
                    postId,
                    postType,
                    b.getBookmarkedAt()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        bookmarkMapper.delete(bookmarkId);
    }
}
