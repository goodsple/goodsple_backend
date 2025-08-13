/**
 * 파일 경로: src/main/java/com/goodsple/common/dto/PagedResponse.java
 * 설명: 페이지네이션된 목록 응답을 위한 공통 래퍼 DTO입니다.
 */
package com.goodsple.common.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Schema(description = "페이지네이션 응답 래퍼 DTO")
@Getter
public class PagedResponse<T> {
    @Schema(description = "콘텐츠 목록")
    private final List<T> content;
    @Schema(description = "현재 페이지 번호", example = "0")
    private final int currentPage;
    @Schema(description = "총 페이지 수", example = "10")
    private final int totalPages;
    @Schema(description = "총 항목 수", example = "95")
    private final long totalElements;

    public PagedResponse(List<T> content, int currentPage, int totalPages, long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}