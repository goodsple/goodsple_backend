//package com.goodsple.features.upload.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Set;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class UploadService {
//
//    @Value("${file.upload-dir}") // application.yml에 있는 값 주입됨
//    private String uploadDir;
//
//    // 허용할 업로드 타입 목록
//    private static final Set<String> ALLOWED_TYPES = Set.of("profile", "post", "product");
//
//    public String upload(MultipartFile file, String type) {
//        // 0. 업로드 타입 검증
//        if (!ALLOWED_TYPES.contains(type)) {
//            throw new IllegalArgumentException("허용되지 않은 업로드 타입입니다: " + type);
//        }
//
//        // 1. 원본 파일명 정리 및 보안 검사
//        String originalFilename = StringUtils.cleanPath(
//                file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
//        if (originalFilename.isBlank() || originalFilename.contains("..")) {
//            throw new IllegalArgumentException("잘못된 파일명입니다: " + originalFilename);
//        }
//
//        // 2. UUID + 원본이름 조합하여 상대경로 생성
//        String uuid = UUID.randomUUID().toString();
//        String relativePath = type + "/" + uuid + "_" + originalFilename;
//
//        // 3. OS에 상관없이 경로 결합 및 정규화
//        Path target = Paths.get(uploadDir)
//                .resolve(relativePath)
//                .normalize();
//
//        try {
//            // 4. 부모 폴더가 없으면 한 번에 생성
//            Files.createDirectories(target.getParent());
//            // 5. 실제로 디스크에 쓰기
//            file.transferTo(target.toFile());
//        } catch (IOException e) {
//            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
//        }
//
//        // 6. 클라이언트에서 접근 가능한 URL 반환
//        return "/uploads/" + relativePath;
//    }
//}

package com.goodsple.features.upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.allowed-types}")
    private String allowedTypesStr;

    // 허용할 업로드 타입 목록을 yml에서 동적으로 가져오기
    private Set<String> getAllowedTypes() {
        return Set.of(allowedTypesStr.split(","));
    }

    public String upload(MultipartFile file, String type) {
        // 0. 업로드 타입 검증
        if (!getAllowedTypes().contains(type)) {
            throw new IllegalArgumentException("허용되지 않은 업로드 타입입니다: " + type);
        }

        // 1. 원본 파일명 정리 및 보안 검사
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        if (originalFilename.isBlank() || originalFilename.contains("..")) {
            throw new IllegalArgumentException("잘못된 파일명입니다: " + originalFilename);
        }

        // 2. UUID + 원본이름 조합하여 S3 키 생성
        String uuid = UUID.randomUUID().toString();
        String s3Key = type + "/" + uuid + "_" + originalFilename;

        try {
            // 3. S3 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 4. S3에 업로드 (퍼블릭 읽기 권한 설정)
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    s3Key,
                    file.getInputStream(),
                    metadata
            );

            s3Client.putObject(putObjectRequest);

            // 5. S3 URL 반환
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }
}