/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/service/AdminAuctionService.java
 * 설명: 관리자용 경매 관리 기능의 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
package com.goodsple.features.admin.auction.service;

import com.goodsple.features.admin.auction.dto.request.AuctionCreateRequest;
import com.goodsple.features.admin.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.admin.auction.dto.request.AuctionUpdateRequest;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminDetailResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminResultResponse;
import com.goodsple.features.admin.auction.entity.Auction;
import com.goodsple.features.admin.auction.mapper.AuctionMapper;
import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.service.AuctionRealtimeService;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAuctionService {

    private final AuctionMapper auctionMapper;
    private final AuctionRealtimeService auctionRealtimeService;
    private final SimpMessagingTemplate messagingTemplate; // [추가] SimpMessagingTemplate 의존성 주입

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

//    @Transactional(readOnly = true)
//    public AuctionAdminDetailResponse getAuctionDetail(Long auctionId) {
//        // return auctionMapper.findAuctionDetailById(auctionId)
//        //        .orElseThrow(() -> new IllegalArgumentException("Auction not found with id: " + auctionId));
//
//        System.out.println("경매 상세 조회 로직 호출됨: ID " + auctionId);
//        return new AuctionAdminDetailResponse(); // 임시 반환
//    }

    @Transactional(readOnly = true)
    public AuctionAdminDetailResponse getAuctionDetail(Long auctionId) {
        return auctionMapper.findAuctionDetailById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));
    }

//    @Transactional
//    public void updateAuction(Long auctionId, AuctionUpdateRequest request) {
//        // TODO: 수정하려는 경매가 존재하는지, 상태가 '예정'인지 확인
//        // TODO: 시간 유효성 및 중복 검증
//        // Auction auction = convertToEntity(request);
//        // auction.setId(auctionId);
//        // auctionMapper.updateAuction(auction);
//        // ... 이미지 업데이트 로직 ...
//
//        System.out.println("경매 수정 로직 호출됨: ID " + auctionId);
//    }

    @Transactional
    public void updateAuction(Long auctionId, AuctionUpdateRequest request) {
        // 1. 수정할 경매가 존재하는지, 수정 가능한 상태인지 확인
        Auction existingAuction = auctionMapper.findAuctionForUpdate(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));

        // '예정(scheduled)' 또는 '중지(cancelled)' 상태가 아닐 경우에만 에러를 발생시킵니다.
        String currentStatus = existingAuction.getAuctionStatus().toLowerCase();
        if (!"scheduled".equals(currentStatus) && !"cancelled".equals(currentStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예정 또는 중지 상태의 경매만 수정할 수 있습니다.");
        }

        // 2. 시간 유효성 검사
        if (request.startTime.isAfter(request.endTime) || request.startTime.isEqual(request.endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        // '예정' 상태의 경매를 수정할 때만 시작 시간이 미래인지 검사합니다.
        if ("scheduled".equals(currentStatus) && request.startTime.isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예정 상태 경매의 시작 시간은 미래여야 합니다.");
        }
        // TODO: 시간 중복 검사 시 자기 자신은 제외하는 로직 추가 필요

        // 3. DTO를 Auction 엔티티로 변환하여 정보 업데이트
        Auction auctionToUpdate = Auction.builder()
                .auctionTitle(request.productName)
                .auctionDescription(request.description)
                .auctionStartPrice(request.startPrice)
                .auctionMinBidUnit(request.minBidUnit)
                .auctionStartTime(request.startTime)
                .auctionEndTime(request.endTime)
                .build();
        auctionToUpdate.setAuctionId(auctionId); // WHERE 절에 사용할 ID 설정

        auctionMapper.updateAuction(auctionToUpdate);

        // 4. 이미지 정보 업데이트 (기존 이미지 모두 삭제 후 새로 추가)
        auctionMapper.deleteAuctionImages(auctionId);
        if (request.imageUrls != null && !request.imageUrls.isEmpty()) {
            for (String url : request.imageUrls) {
                auctionMapper.insertAuctionImage(auctionId, url);
            }
        }
    }

    @Transactional
    public void updateAuctionStatus(Long auctionId, String newStatus) { // [수정] 변수명 명확화 (status -> newStatus)
        // 1. 수정할 경매가 존재하는지 확인
        Auction auction = auctionMapper.findAuctionForUpdate(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));

        // 2. [추가] 이미 같은 상태이면 아무 작업도 하지 않고 리턴 (불필요한 DB 업데이트 및 알림 방지)
        if (auction.getAuctionStatus().equalsIgnoreCase(newStatus)) {
            return;
        }

        // 3. 유효한 상태 값인지 확인하는 로직 (선택 사항이지만 권장)
        // 예: Enum.valueOf(AuctionStatusEnum.class, newStatus.toUpperCase());

        // 4. 상태 업데이트 쿼리 실행
        auctionMapper.updateAuctionStatus(auctionId, newStatus);

        // 5. [추가] 상태 변경에 따른 후속 조치: WebSocket으로 참여자에게 알림 전송
        String message;
        String type;

        // newStatus 값에 따라 메시지와 타입을 결정합니다.
        // 프론트엔드와 약속된 타입(예: AUCTION_CANCELLED, AUCTION_STOPPED)을 사용합니다.
        switch (newStatus.toLowerCase()) {
            case "cancelled":
                message = "경매가 관리자에 의해 취소되었습니다.";
                type = "AUCTION_CANCELLED";
                break;
            // '중지' 상태가 있다면 여기에 추가할 수 있습니다.
            // case "stopped":
            //     message = "경매가 관리자에 의해 일시 중지되었습니다.";
            //     type = "AUCTION_STOPPED";
            //     break;
            default:
                // 다른 상태 변경(예: scheduled -> active)은 스케줄러가 담당하므로
                // 여기서는 별도의 알림을 보내지 않거나, 필요 시 추가할 수 있습니다.
                return;
        }

        // 알림 메시지를 담을 DTO(Map) 생성
        Map<String, String> payload = Map.of(
                "type", type,
                "message", message
        );

        // 해당 경매의 토픽을 구독 중인 모든 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, payload);
    }

    @Transactional(readOnly = true)
    public AuctionAdminResultResponse getAuctionResult(Long auctionId) {
        return auctionMapper.findAuctionResultById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매 결과를 찾을 수 없습니다. ID: " + auctionId));
    }

    /**
     * 특정 경매의 실시간 상태를 Redis에 초기화합니다. (테스트용)
     * @param auctionId 초기화할 경매 ID
     */
    @Transactional
    public void initializeAuctionInRedis(Long auctionId) {
        // 1. DB에서 경매 상태를 'active'로 변경
        auctionMapper.updateAuctionStatus(auctionId, "active");

        // 2. Redis에 저장할 초기 정보 조회
        AuctionState initialState = auctionMapper.findInitialAuctionStateById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "경매 정보를 찾을 수 없습니다. ID: " + auctionId));

        // 3. 실시간 서비스의 startAuction 메소드 호출
        auctionRealtimeService.startAuction(initialState);
    }
}