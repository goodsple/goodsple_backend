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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AdminAuctionService.class);

    private final AuctionMapper auctionMapper;
    private final AuctionRealtimeService auctionRealtimeService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(readOnly = true)
    public PagedResponse<AuctionAdminListResponse> getAuctionList(AuctionSearchRequest searchRequest) {
        long totalElements = auctionMapper.countAuctions(searchRequest);

        List<AuctionAdminListResponse> auctions = auctionMapper.findAuctions(searchRequest);

        int totalPages = (int) Math.ceil((double) totalElements / searchRequest.getSize());

        return new PagedResponse<>(auctions, searchRequest.getPage(), totalPages, totalElements);
    }

    @Transactional
    public Long createAuction(AuctionCreateRequest request) {
        if (request.startTime.isAfter(request.endTime) || request.startTime.isEqual(request.endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        int overlappingCount = auctionMapper.checkOverlappingAuction(request.startTime, request.endTime);
        if (overlappingCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 시간에 이미 다른 경매가 예정되어 있습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminUserId = userDetails.getUserId();

        final String initialStatus = request.startTime.isAfter(OffsetDateTime.now()) ? "scheduled" : "active";

        Auction auction = Auction.builder()
                .userId(adminUserId)
                .auctionTitle(request.productName)
                .auctionDescription(request.description)
                .auctionStartPrice(request.startPrice)
                .auctionMinBidUnit(request.minBidUnit)
                .auctionStartTime(request.startTime)
                .auctionEndTime(request.endTime)
                .auctionStatus(initialStatus)
                .build();

        auctionMapper.insertAuction(auction);
        Long newAuctionId = auction.getAuctionId();

        if (request.imageUrls != null && !request.imageUrls.isEmpty()) {
            for (String url : request.imageUrls) {
                auctionMapper.insertAuctionImage(newAuctionId, url);
            }
        }

        if ("active".equals(initialStatus)) {
            AuctionState initialState = new AuctionState(
                    newAuctionId,
                    request.startPrice,
                    request.minBidUnit,
                    "없음",
                    -1L,
                    request.endTime
            );

            auctionRealtimeService.startAuction(initialState);
            log.info("생성된 경매 ID {}를 즉시 시작합니다.", newAuctionId);
        }

        return newAuctionId;
    }

    @Transactional(readOnly = true)
    public AuctionAdminDetailResponse getAuctionDetail(Long auctionId) {
        return auctionMapper.findAuctionDetailById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));
    }

    @Transactional
    public void updateAuction(Long auctionId, AuctionUpdateRequest request) {
        Auction existingAuction = auctionMapper.findAuctionForUpdate(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));

        String currentStatus = existingAuction.getAuctionStatus().toLowerCase();
        if (!"scheduled".equals(currentStatus) && !"cancelled".equals(currentStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예정 또는 중지 상태의 경매만 수정할 수 있습니다.");
        }

        if (request.startTime.isAfter(request.endTime) || request.startTime.isEqual(request.endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        if ("scheduled".equals(currentStatus) && request.startTime.isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예정 상태 경매의 시작 시간은 미래여야 합니다.");
        }
        // TODO: 시간 중복 검사 시 자기 자신은 제외하는 로직 추가 필요

        Auction auctionToUpdate = Auction.builder()
                .auctionTitle(request.productName)
                .auctionDescription(request.description)
                .auctionStartPrice(request.startPrice)
                .auctionMinBidUnit(request.minBidUnit)
                .auctionStartTime(request.startTime)
                .auctionEndTime(request.endTime)
                .build();
        auctionToUpdate.setAuctionId(auctionId);

        auctionMapper.updateAuction(auctionToUpdate);

        auctionMapper.deleteAuctionImages(auctionId);
        if (request.imageUrls != null && !request.imageUrls.isEmpty()) {
            for (String url : request.imageUrls) {
                auctionMapper.insertAuctionImage(auctionId, url);
            }
        }
    }

    @Transactional
    public void updateAuctionStatus(Long auctionId, String newStatus) {
        Auction auction = auctionMapper.findAuctionForUpdate(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 경매를 찾을 수 없습니다. ID: " + auctionId));

        if (auction.getAuctionStatus().equalsIgnoreCase(newStatus)) {
            return;
        }

        auctionMapper.updateAuctionStatus(auctionId, newStatus);

        final String statusLower = newStatus.toLowerCase();

        if ("cancelled".equals(statusLower)) {
            Map<String, String> payload = Map.of("type", "AUCTION_CANCELLED", "message", "경매가 관리자에 의해 중지되었습니다.");
            messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, payload);

            auctionRealtimeService.removeAuctionFromRedis(auctionId);
            log.info("중지된 경매 ID {}의 Redis 데이터를 삭제합니다.", auctionId);

        } else if ("active".equals(statusLower)) {
            AuctionState initialState = auctionMapper.findInitialAuctionStateById(auctionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "경매 정보를 찾을 수 없습니다. ID: " + auctionId));
            auctionRealtimeService.startAuction(initialState);
            log.info("경매 ID {}를 관리자가 재개합니다.", auctionId);
        }
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
        auctionMapper.updateAuctionStatus(auctionId, "active");

        AuctionState initialState = auctionMapper.findInitialAuctionStateById(auctionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "경매 정보를 찾을 수 없습니다. ID: " + auctionId));

        auctionRealtimeService.startAuction(initialState);
    }
}