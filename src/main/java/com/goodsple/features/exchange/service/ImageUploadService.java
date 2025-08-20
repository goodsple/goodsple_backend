package com.goodsple.features.exchange.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {

  /**
   * 이미지를 업로드하고 업로드된 이미지의 URL을 반환합니다.
   * @param file 업로드할 이미지 파일
   * @return 업로드된 이미지의 URL
   */
  String uploadImage(MultipartFile file);

  /**
   * S3에서 이미지들을 삭제합니다.
   * @param imageUrls 삭제할 이미지 URL 리스트
   */
  void deleteImagesFromS3(List<String> imageUrls);

}
