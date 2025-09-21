package com.goodsple.features.bookmark.controller;

import com.goodsple.features.bookmark.dto.BookmarkInfoResponse;
import com.goodsple.features.bookmark.dto.BookmarkRequest;
import com.goodsple.features.bookmark.dto.BookmarkResponse;
import com.goodsple.features.bookmark.dto.MoveBookmarkRequest;
import com.goodsple.features.bookmark.mapper.BookmarkMapper;
import com.goodsple.features.bookmark.service.BookmarkService;
import com.goodsple.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BookmarkMapper bookmarkMapper;

    // 북마크 추가
    @PostMapping
    public ResponseEntity<?> addBookmark(
            @Valid @RequestBody BookmarkRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = extractUserIdFromHeader(authorizationHeader);
            BookmarkResponse response = bookmarkService.addBookmark(userId, request);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 유저 북마크 조회
    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getUserBookmarks(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = extractUserIdFromHeader(authorizationHeader);
        List<BookmarkResponse> response = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(response);
    }

    // 북마크 삭제
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(
            @PathVariable Long bookmarkId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = extractUserIdFromHeader(authorizationHeader);
        try {
            bookmarkService.deleteBookmark(userId, bookmarkId);
            return ResponseEntity.ok(Map.of("message", "북마크가 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        }
    }

    // 폴더 내 북마크 조회
    @GetMapping("/folders/{folderId}")
    public ResponseEntity<List<BookmarkResponse>> getBookmarksInFolder(
            @PathVariable Long folderId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = extractUserIdFromHeader(authorizationHeader);
        List<BookmarkResponse> response = bookmarkService.getBookmarksByUserAndFolder(userId, folderId);
        return ResponseEntity.ok(response);
    }

    // 북마크 이동
    @PostMapping("/move")
    public ResponseEntity<?> moveBookmarks(
            @Valid @RequestBody MoveBookmarkRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = extractUserIdFromHeader(authorizationHeader);
        bookmarkService.moveBookmarks(userId, request.getBookmarkIds(), request.getTargetFolderId());
        return ResponseEntity.ok(Map.of("message", "북마크가 이동되었습니다."));
    }

    // 게시글 북마크 정보
    @GetMapping("/{postId}/bookmark-info")
    public ResponseEntity<BookmarkInfoResponse> getBookmarkInfo(
            @PathVariable Long postId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) String postType) { // optional로 변경

        Long userId = extractUserIdFromHeader(authorizationHeader);
        BookmarkInfoResponse response = bookmarkService.getBookmarkInfo(userId, postId, postType);
        return ResponseEntity.ok(response);
    }


    // ---------------- helper ----------------
    private Long extractUserIdFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효한 Authorization 헤더가 필요합니다.");
        }
        String token = authorizationHeader.substring(7);
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
