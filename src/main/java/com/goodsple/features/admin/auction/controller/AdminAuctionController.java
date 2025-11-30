package com.goodsple.features.admin.auction.controller;

import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.admin.auction.dto.request.AuctionCreateRequest;
import com.goodsple.features.admin.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.admin.auction.dto.request.AuctionStatusUpdateRequest;
import com.goodsple.features.admin.auction.dto.request.AuctionUpdateRequest;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminDetailResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminResultResponse;
import com.goodsple.features.admin.auction.service.AdminAuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@Tag(name = "02. 경매 관리 (Admin)", description = "관리자용 경매 생성, 조회, 수정, 상태 변경 API")
@RestController
@RequestMapping("/api/admin/auctions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuctionController {

    private final AdminAuctionService adminAuctionService;

    @Operation(summary = "경매 목록 조회", description = "다양한 조건으로 경매 목록을 검색하고 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<AuctionAdminListResponse>> getAuctionList(
            @Parameter(description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY) @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", in = ParameterIn.QUERY) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "상품명 검색어", in = ParameterIn.QUERY) @RequestParam(required = false) String productName,
            @Parameter(description = "경매 상태 필터", in = ParameterIn.QUERY) @RequestParam(required = false) String status,
            @Parameter(description = "검색 시작 날짜 (YYYY-MM-DD)", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "검색 종료 날짜 (YYYY-MM-DD)", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        AuctionSearchRequest searchRequest = new AuctionSearchRequest();
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setProductName(productName);
        searchRequest.setStatus(status);
        searchRequest.setStartDate(startDate);
        searchRequest.setEndDate(endDate);

        PagedResponse<AuctionAdminListResponse> response = adminAuctionService.getAuctionList(searchRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "경매 상세 정보 조회", description = "경매 수정을 위해 특정 경매의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 경매를 찾을 수 없음")
    })
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionAdminDetailResponse> getAuctionDetail(
            @Parameter(name = "auctionId", description = "조회할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId) {
        AuctionAdminDetailResponse response = adminAuctionService.getAuctionDetail(auctionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신규 경매 생성", description = "새로운 라이브 경매를 시스템에 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<Void> createAuction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 경매의 정보 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuctionCreateRequest.class))
            )
            @Valid @RequestBody AuctionCreateRequest createRequest) {
        Long auctionId = adminAuctionService.createAuction(createRequest);
        URI location = URI.create(String.format("/api/admin/auctions/%d", auctionId));
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "경매 정보 수정", description = "기존 경매의 정보를 수정합니다. (경매 시작 전만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 수정 불가능한 상태"),
            @ApiResponse(responseCode = "404", description = "해당 경매를 찾을 수 없음")
    })
    @PutMapping("/{auctionId}")
    public ResponseEntity<Void> updateAuction(
            @Parameter(name = "auctionId", description = "수정할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId,
            @Valid @RequestBody AuctionUpdateRequest updateRequest) {
        adminAuctionService.updateAuction(auctionId, updateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "경매 상태 변경", description = "경매 상태를 강제로 변경합니다. (예: 중지, 취소)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 경매를 찾을 수 없음")
    })
    @PatchMapping("/{auctionId}/status")
    public ResponseEntity<Void> updateAuctionStatus(
            @Parameter(name = "auctionId", description = "상태를 변경할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId,
            @Valid @RequestBody AuctionStatusUpdateRequest statusRequest) {
        adminAuctionService.updateAuctionStatus(auctionId, statusRequest.getStatus());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "경매 결과 상세 조회", description = "종료된 경매의 최종 결과(낙찰자, 입찰 내역 등)를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 경매 결과를 찾을 수 없음")
    })
    @GetMapping("/{auctionId}/result")
    public ResponseEntity<AuctionAdminResultResponse> getAuctionResult(
            @Parameter(name = "auctionId", description = "결과를 조회할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId) {
        AuctionAdminResultResponse response = adminAuctionService.getAuctionResult(auctionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[테스트용] 경매 실시간 시작", description = "특정 경매를 '진행중' 상태로 바꾸고 Redis에 실시간 데이터를 초기화합니다.")
    @PostMapping("/{auctionId}/start-realtime")
    public ResponseEntity<Void> startAuctionRealtime(
            @Parameter(name = "auctionId", description = "시작할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId) {

        adminAuctionService.initializeAuctionInRedis(auctionId);
        return ResponseEntity.ok().build();
    }
}