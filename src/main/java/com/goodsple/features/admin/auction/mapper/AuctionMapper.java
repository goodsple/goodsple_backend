/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/mapper/AuctionMapper.java
 * 설명: 경매 관련 데이터베이스 쿼리를 호출하는 MyBatis 매퍼 인터페이스입니다.
 */
package com.goodsple.features.admin.auction.mapper;

import com.goodsple.features.admin.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminDetailResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.admin.auction.dto.response.AuctionAdminResultResponse;
import com.goodsple.features.admin.auction.dto.response.BidHistoryInfo;
import com.goodsple.features.admin.auction.entity.Auction;
import com.goodsple.features.auction.dto.AuctionState;
import com.goodsple.features.auction.dto.response.AuctionPageDataResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface AuctionMapper {

    /**
     * 관리자 페이지에서 경매 목록을 조건에 따라 조회합니다.
     * @param searchRequest 검색 조건 DTO
     * @return 경매 목록
     */
    List<AuctionAdminListResponse> findAuctions(AuctionSearchRequest searchRequest);

    /**
     * 관리자 페이지에서 경매 목록의 전체 개수를 조건에 따라 조회합니다.
     * @param searchRequest 검색 조건 DTO
     * @return 전체 개수
     */
    long countAuctions(AuctionSearchRequest searchRequest);

    // TODO: 아래 메소드들에 대한 실제 구현 필요
    // void insertAuction(Auction auction);
    // void insertAuctionImage(@Param("auctionId") Long auctionId, @Param("imageUrl") String imageUrl);
    void insertAuction(Auction auction);
    void insertAuctionImage(@Param("auctionId") Long auctionId, @Param("imageUrl") String imageUrl);
    int checkOverlappingAuction(@Param("startTime") OffsetDateTime startTime, @Param("endTime") OffsetDateTime endTime);
    // Optional<AuctionAdminDetailResponse> findAuctionDetailById(Long auctionId);
    // void updateAuction(Auction auction);
    // void updateAuctionStatus(@Param("auctionId") Long auctionId, @Param("status") String status);
    /**
     * 수정 페이지에 필요한 경매 상세 정보와 이미지 목록을 조회합니다.
     * @param auctionId 조회할 경매 ID
     * @return 경매 상세 정보 DTO
     */
    Optional<AuctionAdminDetailResponse> findAuctionDetailById(Long auctionId);

    /**
     * 수정을 위해 경매 엔티티를 조회합니다. (상태 확인 등)
     * @param auctionId 조회할 경매 ID
     * @return 경매 엔티티
     */
    Optional<Auction> findAuctionForUpdate(Long auctionId);

    /**
     * 경매 정보를 업데이트합니다.
     * @param auction 수정할 정보가 담긴 경매 엔티티
     */
    void updateAuction(Auction auction);

    /**
     * 특정 경매에 연결된 모든 이미지를 삭제합니다. (이미지 수정 시 사용)
     * @param auctionId 경매 ID
     */
    void deleteAuctionImages(Long auctionId);

    /**
     * 관리자용 경매 결과 상세 페이지에 필요한 모든 정보를 조회합니다.
     * @param auctionId 조회할 경매 ID
     * @return 경매 결과 정보 DTO
     */
    Optional<AuctionAdminResultResponse> findAuctionResultById(Long auctionId);

    /**
     * 특정 경매의 전체 입찰 기록을 조회합니다. (서브 쿼리용)
     * @param auctionId 조회할 경매 ID
     * @return 입찰 기록 리스트
     */
    List<BidHistoryInfo> findBidHistoryByAuctionId(Long auctionId);

    /**
     * 특정 경매의 상태를 변경합니다.
     * @param auctionId 상태를 변경할 경매 ID
     * @param status 새로운 상태 값 (예: cancelled, active)
     */
    void updateAuctionStatus(@Param("auctionId") Long auctionId, @Param("status") String status);

    Optional<AuctionPageDataResponse> findAuctionPageDataById(Long auctionId);

    /**
     * Redis에 저장할 경매 초기 상태 정보를 DB에서 조회합니다.
     * @param auctionId 경매 ID
     * @return 경매 초기 상태 DTO
     */
    Optional<AuctionState> findInitialAuctionStateById(Long auctionId);

    List<AuctionState> findAuctionsToStart(OffsetDateTime now);

    List<Long> findAuctionsToEnd(OffsetDateTime now);

    void updateAuctionWinner(@Param("auctionId") Long auctionId, @Param("winnerId") Long winnerId, @Param("finalPrice") BigDecimal finalPrice);
}
