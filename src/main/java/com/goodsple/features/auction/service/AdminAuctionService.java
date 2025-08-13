/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/AdminAuctionService.java
 * 설명: 관리자용 경매 관리 기능의 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
package com.goodsple.features.auction.service;

import com.goodsple.features.auction.dto.request.AuctionCreateRequest;
import com.goodsple.features.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.auction.dto.request.AuctionUpdateRequest;
import com.goodsple.features.auction.dto.response.AuctionAdminDetailResponse;
import com.goodsple.features.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.auction.entity.Auction;
import com.goodsple.features.auction.mapper.AuctionMapper;
import com.goodsple.common.dto.PagedResponse;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuctionService {

    private final AuctionMapper auctionMapper;

    @Transactional(readOnly = true)
    public PagedResponse<AuctionAdminListResponse> getAuctionList(AuctionSearchRequest searchRequest) {
        // 페이지네이션을 위한 전체 카운트 조회
        long totalElements = auctionMapper.countAuctions(searchRequest);

        // 실제 목록 조회
        List<AuctionAdminListResponse> auctions = auctionMapper.findAuctions(searchRequest);

        int totalPages = (int) Math.ceil((double) totalElements / searchRequest.getSize());

        return new PagedResponse<>(auctions, searchRequest.getPage(), totalPages, totalElements);
    }

//    @Transactional
//    public Long createAuction(AuctionCreateRequest request) {
//        // TODO: 시간 유효성 검증 (endTime > startTime)
//        // TODO: 다른 경매와 시간 겹치는지 검증 (REQ-SYS-002)
//
//        // DTO를 Auction 엔티티(DB 테이블 매핑용 객체)로 변환하는 로직 필요
//        // Auction auction = convertToEntity(request);
//        // auctionMapper.insertAuction(auction);
//        // Long auctionId = auction.getId();
//        // for (String url : request.imageUrls) {
//        //     auctionMapper.insertAuctionImage(auctionId, url);
//        // }
//        // return auctionId;
//
//        System.out.println("경매 생성 로직 호출됨: " + request.productName);
//        return 1L; // 임시 반환
//    }

    @Transactional
    public Long createAuction(AuctionCreateRequest request) {
        // 1. 시간 유효성 검증
        if (request.startTime.isAfter(request.endTime) || request.startTime.isEqual(request.endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        // 2. 다른 경매와 시간 겹치는지 검증 (REQ-SYS-002)
        int overlappingCount = auctionMapper.checkOverlappingAuction(request.startTime, request.endTime);
        if (overlappingCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 시간에 이미 다른 경매가 예정되어 있습니다.");
        }

        // 3. 현재 로그인한 관리자 ID 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminUserId = userDetails.getUserId();

        // 4. DTO를 Auction 엔티티로 변환
        Auction auction = Auction.builder()
                .userId(adminUserId)
                .auctionTitle(request.productName)
                .auctionDescription(request.description)
                .auctionStartPrice(request.startPrice)
                .auctionMinBidUnit(request.minBidUnit)
                .auctionStartTime(request.startTime)
                .auctionEndTime(request.endTime)
                .auctionStatus("scheduled") // 생성 시 기본 상태는 '예정'
                .build();

        // 5. 경매 정보 저장 (useGeneratedKeys에 의해 auction 객체에 auctionId가 채워짐)
        auctionMapper.insertAuction(auction);
        Long newAuctionId = auction.getAuctionId();

        // 6. 이미지 URL들 저장
        if (request.imageUrls != null && !request.imageUrls.isEmpty()) {
            for (String url : request.imageUrls) {
                auctionMapper.insertAuctionImage(newAuctionId, url);
            }
        }

        return newAuctionId;
    }

    @Transactional(readOnly = true)
    public AuctionAdminDetailResponse getAuctionDetail(Long auctionId) {
        // return auctionMapper.findAuctionDetailById(auctionId)
        //        .orElseThrow(() -> new IllegalArgumentException("Auction not found with id: " + auctionId));

        System.out.println("경매 상세 조회 로직 호출됨: ID " + auctionId);
        return new AuctionAdminDetailResponse(); // 임시 반환
    }

    @Transactional
    public void updateAuction(Long auctionId, AuctionUpdateRequest request) {
        // TODO: 수정하려는 경매가 존재하는지, 상태가 '예정'인지 확인
        // TODO: 시간 유효성 및 중복 검증
        // Auction auction = convertToEntity(request);
        // auction.setId(auctionId);
        // auctionMapper.updateAuction(auction);
        // ... 이미지 업데이트 로직 ...

        System.out.println("경매 수정 로직 호출됨: ID " + auctionId);
    }

    @Transactional
    public void updateAuctionStatus(Long auctionId, String status) {
        // TODO: 경매 존재 여부 확인
        // auctionMapper.updateAuctionStatus(auctionId, status);
        // TODO: 상태 변경에 따른 후속 조치 (예: WebSocket으로 참여자에게 알림)

        System.out.println("경매 상태 변경 로직 호출됨: ID " + auctionId + ", 상태 " + status);
    }
}