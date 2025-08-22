package com.goodsple.features.bookmark.controller;

import com.goodsple.features.bookmark.dto.BookmarkRequest;
import com.goodsple.features.bookmark.dto.BookmarkResponse;
import com.goodsple.features.bookmark.service.BookmarkService;
import com.goodsple.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<BookmarkResponse> addBookmark(
            @Valid @RequestBody BookmarkRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        BookmarkResponse response = bookmarkService.addBookmark(userId, request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getUserBookmarks(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        List<BookmarkResponse> response = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId
    ) {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
