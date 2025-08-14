package com.goodsple.features.upload.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.goodsple.features.upload.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/test")
    public String testS3() {
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("ap-northeast-2").build();
            return s3.doesBucketExist("goodsple-0814") ? "연결 성공!" : "버킷 없음";
        } catch (Exception e) {
            return "에러: " + e.getMessage();
        }
    }

    // 어노테이션 주로 사용되는 상황 설명
    // @RequestParam / 폼 데이터나 쿼리 파라미터 (application/x-www-form-urlencoded),	단순한 문자열, 숫자 등
    // @RequestBody / application/json 요청 본문,	JSON 객체 전체를 바인딩
    // @RequestPart / multipart/form-data 요청에서 파일과 JSON 일부 추출,	파일 업로드 시 가장 많이 사용됨

}
