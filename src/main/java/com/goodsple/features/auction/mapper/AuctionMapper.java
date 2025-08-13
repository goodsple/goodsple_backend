/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/mapper/AuctionMapper.java
 * 설명: 경매 관련 데이터베이스 쿼리를 호출하는 MyBatis 매퍼 인터페이스입니다.
 */
package com.goodsple.features.auction.mapper;

import com.goodsple.features.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.auction.dto.response.AuctionAdminDetailResponse;
import com.goodsple.features.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.auction.entity.Auction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
