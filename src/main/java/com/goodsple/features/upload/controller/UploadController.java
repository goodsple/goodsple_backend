package com.goodsple.features.upload.controller;

import com.goodsple.features.upload.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "파일 업로드 API")
public class UploadController {
    private final UploadService uploadService;

    /**
     * 타입(profile, post, review 등)에 따라 업로드
     * - 파일 파라미터 이름은 반드시 "file"
     * - consumes 설정으로 Swagger/프론트 혼선 방지
     */
    @PostMapping(
            path = "/{type}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable String type,
            @RequestPart("file") MultipartFile file
    ) {
        // 1) 파일 유효성
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어 있습니다."));
        }

        // 2) 업로드 (권장: service는 key를 반환)
        String key = uploadService.upload(file, type);

        // 3) URL 필요 시(퍼블릭 버킷/ACL일 때만)
        //    비공개면 presigned-GET API 따로 쓰세요.
        String maybeUrl = null;
        try {
            maybeUrl = uploadService.publicUrl(key); // 퍼블릭이 아닐 수 있으니 실패 안 나게 구현해두세요.
        } catch (Exception ignored) {}

        // 4) 응답: key는 항상 주고, url은 가능할 때만
        Map<String, String> body = (maybeUrl == null)
                ? Map.of("key", key)
                : Map.of("key", key, "url", maybeUrl);

        // 201 Created + Location 헤더(key 기반) 선택(선호)
        return ResponseEntity
                .created(URI.create("/api/upload/" + type)) // 리소스 조회 엔드포인트 있으면 그걸로 변경
                .body(body);
    }

    // 어노테이션 주로 사용되는 상황 설명
    // @RequestParam / 폼 데이터나 쿼리 파라미터 (application/x-www-form-urlencoded),	단순한 문자열, 숫자 등
    // @RequestBody / application/json 요청 본문,	JSON 객체 전체를 바인딩
    // @RequestPart / multipart/form-data 요청에서 파일과 JSON 일부 추출,	파일 업로드 시 가장 많이 사용됨

}

