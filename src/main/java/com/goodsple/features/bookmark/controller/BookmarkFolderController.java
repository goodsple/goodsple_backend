package com.goodsple.features.bookmark.controller;

import com.goodsple.features.bookmark.dto.FolderCreationRequest;
import com.goodsple.features.bookmark.entity.BookmarkFolder;
import com.goodsple.features.bookmark.service.BookmarkFolderService;
import com.goodsple.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap; // 수정됨⭐
import java.util.List;
import java.util.Map;     // 수정됨⭐

@RestController
@RequestMapping("/api/bookmark-folders")
public class BookmarkFolderController {

    private final BookmarkFolderService bookmarkFolderService;
    private final JwtTokenProvider jwtTokenProvider;

    public BookmarkFolderController(BookmarkFolderService bookmarkFolderService, JwtTokenProvider jwtTokenProvider) {
        this.bookmarkFolderService = bookmarkFolderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 기존 폴더 가져오기
    @GetMapping
    public ResponseEntity<?> getUserFolders(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "유효한 Authorization 헤더가 필요합니다.");
            return ResponseEntity.status(401).body(errorMap);
        }

        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        List<BookmarkFolder> folders = bookmarkFolderService.getFoldersByUserId(userId);

        return ResponseEntity.ok(folders);
    }

    // 폴더 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFolder(
            @Valid @RequestBody FolderCreationRequest request,
            BindingResult bindingResult,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "폴더 이름과 색상을 올바르게 입력해주세요.");
            return ResponseEntity.badRequest().body(errorMap);
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("message", "유효한 Authorization 헤더가 필요합니다.");
            return ResponseEntity.status(401).body(errorMap);
        }

        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        Long folderId = bookmarkFolderService.createFolder(request, userId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", "폴더가 성공적으로 생성되었습니다.");
        resultMap.put("folderId", folderId);
        return ResponseEntity.ok(resultMap);
    }

    // 폴더 수정
    @PutMapping("/{folderId}")
    public ResponseEntity<Map<String, String>> editFolder(
            @PathVariable Long folderId,
            @Valid @RequestBody FolderCreationRequest request,
            BindingResult bindingResult,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "폴더 이름과 색상을 올바르게 입력해주세요.");
            return ResponseEntity.badRequest().body(errorMap);
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "유효한 Authorization 헤더가 필요합니다.");
            return ResponseEntity.status(401).body(errorMap);
        }

        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        bookmarkFolderService.editFolder(folderId, request, userId);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("message", "폴더가 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(resultMap);
    }

    // 폴더 삭제
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Map<String, String>> deleteFolder(
            @PathVariable Long folderId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "유효한 Authorization 헤더가 필요합니다.");
            return ResponseEntity.status(401).body(errorMap);
        }

        String token = authorizationHeader.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        bookmarkFolderService.deleteFolder(folderId, userId);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("message", "폴더가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(resultMap);
    }
}
