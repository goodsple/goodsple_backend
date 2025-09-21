package com.goodsple.features.bookmark.service;

import com.goodsple.features.bookmark.dto.BookmarkInfoResponse;
import com.goodsple.features.bookmark.dto.BookmarkRequest;
import com.goodsple.features.bookmark.dto.BookmarkResponse;
import com.goodsple.features.bookmark.entity.Bookmark;
import com.goodsple.features.bookmark.mapper.BookmarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    @Transactional
    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request) {
        // postType이 없는 경우, 자동으로 exchangePostId/eventPostId를 설정 가능
        Long exchangePostId = "exchange".equals(request.getPostType()) ? request.getPostId() : null;
        Long eventPostId = "event".equals(request.getPostType()) ? request.getPostId() : null;

        // postType 없이 요청 시 postId를 무조건 exchangePostId로 처리 (기본값)
        if (exchangePostId == null && eventPostId == null) {
            exchangePostId = request.getPostId();
        }

        // Map으로 파라미터 전달
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("exchangePostId", exchangePostId);
        param.put("eventPostId", eventPostId);

        boolean exists = bookmarkMapper.exists(param);
        if (exists) throw new IllegalArgumentException("이미 북마크된 게시글입니다.");

        Bookmark bookmark = Bookmark.builder()
                .userId(userId)
                .folderId(request.getFolderId())
                .exchangePostId(exchangePostId)
                .eventPostId(eventPostId)
                .bookmarkedAt(OffsetDateTime.now())
                .build();

        bookmarkMapper.save(bookmark);

        // postType이 없으면 자동으로 "exchange"로 설정
        String postType = request.getPostType() != null ? request.getPostType() : "exchange";
        Long postId = exchangePostId != null ? exchangePostId : eventPostId;

        return new BookmarkResponse(
                bookmark.getBookmarkId(),
                bookmark.getFolderId(),
                postId,
                postType,
                bookmark.getBookmarkedAt()
        );
    }

    public List<BookmarkResponse> getUserBookmarks(Long userId) {
        return bookmarkMapper.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void deleteBookmark(Long userId, Long bookmarkId) {
//        // 삭제 전 소유자 확인
//        List<Bookmark> bookmarks = bookmarkMapper.findByUserId(userId);
//        boolean exists = bookmarks.stream().anyMatch(b -> b.getBookmarkId().equals(bookmarkId));
//        if (!exists) throw new IllegalArgumentException("본인 북마크가 아니거나 존재하지 않습니다.");
//        bookmarkMapper.delete(bookmarkId);
//    }

    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("bookmarkId", bookmarkId);

        int deletedRows = bookmarkMapper.deleteByUserAndBookmarkId(param);

        if (deletedRows == 0) {
            throw new IllegalArgumentException("본인 북마크가 아니거나 존재하지 않습니다.");
        }
    }


    public List<BookmarkResponse> getBookmarksByUserAndFolder(Long userId, Long folderId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("folderId", folderId);

        return bookmarkMapper.findByUserIdAndFolderId(param)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookmarkResponse mapToResponse(Bookmark b) {
        String postType = b.getExchangePostId() != null ? "exchange" : "event";
        Long postId = b.getExchangePostId() != null ? b.getExchangePostId() : b.getEventPostId();
        return new BookmarkResponse(
                b.getBookmarkId(),
                b.getFolderId(),
                postId,
                postType,
                b.getBookmarkedAt()
        );
    }

    @Transactional
    public void moveBookmarks(Long userId, List<Long> bookmarkIds, Long targetFolderId) {
        for (Long bookmarkId : bookmarkIds) {
            // 소유자 체크
            List<Bookmark> bookmarks = bookmarkMapper.findByUserId(userId);
            boolean exists = bookmarks.stream().anyMatch(b -> b.getBookmarkId().equals(bookmarkId));
            if (!exists) throw new IllegalArgumentException("본인 북마크가 아니거나 존재하지 않습니다.");

            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("bookmarkId", bookmarkId);
            param.put("targetFolderId", targetFolderId);

            bookmarkMapper.updateFolder(param);
        }
    }

    public int getBookmarkCount(Long postId, String postType) {
        if ("exchange".equals(postType)) {
            return bookmarkMapper.countByExchangePostId(postId);
        } else {
            return bookmarkMapper.countByEventPostId(postId);
        }
    }

    public boolean isBookmarkedByUser(Long userId, Long postId, String postType) {
        if (userId == null || postId == null || postType == null) {
            return false; // 로그인 안 했거나 값이 없으면 북마크 안 됨
        }

        String normalizedPostType = postType.toLowerCase();
        if (!"exchange".equals(normalizedPostType) && !"event".equals(normalizedPostType)) {
            throw new IllegalArgumentException("유효하지 않은 postType: " + postType);
        }

        Long exchangePostId = "exchange".equals(normalizedPostType) ? postId : null;
        Long eventPostId = "event".equals(normalizedPostType) ? postId : null;

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("exchangePostId", exchangePostId);
        param.put("eventPostId", eventPostId);

        return bookmarkMapper.exists(param);
    }

    public BookmarkInfoResponse getBookmarkInfo(Long userId, Long postId, String postType) {
        int exchangeCount = bookmarkMapper.countByExchangePostId(postId);
        int eventCount = bookmarkMapper.countByEventPostId(postId);

        boolean isBookmarked = false;
        if (userId != null) {
            Map<String, Object> paramExchange = new HashMap<>();
            paramExchange.put("userId", userId);
            paramExchange.put("exchangePostId", postId);
            paramExchange.put("eventPostId", null);

            Map<String, Object> paramEvent = new HashMap<>();
            paramEvent.put("userId", userId);
            paramEvent.put("exchangePostId", null);
            paramEvent.put("eventPostId", postId);

            isBookmarked = bookmarkMapper.exists(paramExchange) || bookmarkMapper.exists(paramEvent);
        }

        int totalCount = exchangeCount + eventCount;

        return new BookmarkInfoResponse(totalCount, isBookmarked);
    }

}
