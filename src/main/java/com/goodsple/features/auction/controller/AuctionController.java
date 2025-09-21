/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/controller/AuctionController.java
 * 설명: 사용자용 경매 조회 API를 제공하는 컨트롤러입니다.
 */
package com.goodsple.features.auction.controller;

import com.goodsple.features.auction.dto.response.AuctionPageDataResponse;
import com.goodsple.features.auction.service.UserAuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "03. 경매 (User)", description = "사용자용 경매 조회 및 참여 API")
@RestController
@RequestMapping("/api/auctions") // 관리자용(/api/admin/auctions)과 경로가 다릅니다.
@RequiredArgsConstructor
public class AuctionController {

    private final UserAuctionService userAuctionService;

    @Operation(summary = "라이브 경매 페이지 초기 데이터 조회", description = "사용자가 라이브 경매 페이지에 처음 진입할 때 필요한 모든 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 경매를 찾을 수 없거나 진행중이 아님")
    })
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionPageDataResponse> getAuctionPageData(
            @Parameter(name = "auctionId", description = "조회할 경매의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable Long auctionId) {

        AuctionPageDataResponse response = userAuctionService.getAuctionPageData(auctionId);
        return ResponseEntity.ok(response);
    }
}