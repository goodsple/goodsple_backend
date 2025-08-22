package com.goodsple.features.exchange.controller;

import com.goodsple.features.exchange.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지 업로드", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

  private final ImageUploadService imageUploadService; // ExchangePostService 대신 ImageUploadService 주입

  @Operation(summary = "이미지 업로드", description = "단일 이미지를 업로드하고 저장된 URL을 반환합니다.")
  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    String imageUrl = imageUploadService.uploadImage(file);
    return ResponseEntity.ok(imageUrl);
  }



}
