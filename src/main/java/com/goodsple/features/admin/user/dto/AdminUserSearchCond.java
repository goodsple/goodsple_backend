package com.goodsple.features.admin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "관리자 회원 검색 조건 DTO")
public class AdminUserSearchCond {

    @Schema(description = "회원 ID", example = "101")
    private Long userId;

    @Schema(description = "검색 기준", example = "loginId 또는 nickname")
    private String queryType;

    @Schema(description = "검색 키워드", example = "홍길동")
    private String query;

    @Schema(description = "회원 권한 목록", example = "[\"user\", \"admin\"]")
    private List<String> roles;

    @Schema(description = "회원 상태 목록", example = "[\"active\", \"suspended\", \"withdrawn\"]")
    private List<String> statuses;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "가입일 시작", example = "2025-01-01")
    private LocalDate joinedFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "가입일 끝", example = "2025-12-31")
    private LocalDate joinedTo;

    @Schema(description = "페이지 번호(0-base)", example = "0")
    private Integer page;

    @Schema(description = "페이지 크기", example = "20")
    private Integer size;
}
