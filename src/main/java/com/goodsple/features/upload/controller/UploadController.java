package com.goodsple.features.upload.controller;

import com.goodsple.features.upload.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "사용자 프로필 이미지 업로드 API")
public class UploadController {
    private final UploadService uploadService;

    @PostMapping("/profile")
    public ResponseEntity<Map<String,String>>uploadProfileImage(
            @RequestPart MultipartFile file) {
        // @RequestPart는 multipart/form-data 요청에서 특정 파트를 꺼내는 어노테이션
        String url = uploadService.upload(file, "profile");
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/review")
    public ResponseEntity<Map<String,String>>uploadReviewImage(
            @RequestPart(required = false) MultipartFile file){
        String url = uploadService.upload(file, "review");
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String,String>>uploadPostImage(
            @RequestPart(required = false) MultipartFile file){
        String url = uploadService.upload(file, "post");
        return ResponseEntity.ok(Map.of("url", url));
    }

    // 어노테이션 주로 사용되는 상황 설명
    // @RequestParam / 폼 데이터나 쿼리 파라미터 (application/x-www-form-urlencoded),	단순한 문자열, 숫자 등
    // @RequestBody / application/json 요청 본문,	JSON 객체 전체를 바인딩
    // @RequestPart / multipart/form-data 요청에서 파일과 JSON 일부 추출,	파일 업로드 시 가장 많이 사용됨

}

