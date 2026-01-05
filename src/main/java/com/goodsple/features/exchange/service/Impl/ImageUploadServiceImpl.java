package com.goodsple.features.exchange.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.goodsple.features.exchange.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

  private final AmazonS3 amazonS3;

  @Value("${aws.s3.bucket}")
  private String bucket;

  // 파일 저장 카테고리 (post로 고정)
  private final String uploadCategory = "post";

  @Override
  public String uploadImage(MultipartFile file) {
    if (file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어있습니다.");
    }

    try {
      // 파일명 중복 방지를 위해 UUID와 원본 파일명 사용
      String fileName = uploadCategory + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

      // ObjectMetadata 설정 (파일 타입 및 크기)
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());

      // S3에 파일 업로드
      amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

      // 업로드된 이미지 URL 반환
      return amazonS3.getUrl(bucket, fileName).toString();
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 실패", e);
    }
  }

  /**
   * S3에서 이미지들을 삭제합니다.
   * @param imageUrls 삭제할 이미지 URL 리스트
   */
  @Override // 인터페이스를 구현함을 명시적으로 표시
  public void deleteImagesFromS3(List<String> imageUrls) {
    if (imageUrls == null || imageUrls.isEmpty()) {
      return;
    }

    List<String> fileNames = imageUrls.stream()
        .map(this::getFileNameFromUrl)
        .collect(Collectors.toList());

    for (String fileName : fileNames) {
      amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
  }

  /**
   * S3 URL에서 파일명을 추출합니다.
   * @param imageUrl 이미지 URL
   * @return S3 객체 키 (파일명)
   */
  private String getFileNameFromUrl(String imageUrl) {
    try {
      if (imageUrl == null || imageUrl.isBlank()) {
        throw new IllegalArgumentException("imageUrl is null or blank");
      }

      // 이미 key(post/xxx) 형태인 경우
      if (!imageUrl.startsWith("http")) {
        return imageUrl;
      }

      URI uri = new URI(imageUrl);
      String path = uri.getPath(); // /post/UUID-file.jpg

      if (path == null || !path.contains(uploadCategory + "/")) {
        throw new IllegalArgumentException("Invalid S3 image URL: " + imageUrl);
      }

      // 맨 앞 '/' 제거
      return path.startsWith("/") ? path.substring(1) : path;

    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "S3 URL에서 파일명 추출 실패: " + imageUrl,
          e
      );
    }
  }


}
