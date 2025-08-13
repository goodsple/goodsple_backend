/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/mapper/AuctionMapper.java
 * 설명: 경매 관련 데이터베이스 쿼리를 호출하는 MyBatis 매퍼 인터페이스입니다.
 */
package com.goodsple.features.auction.mapper;

import com.goodsple.features.auction.dto.request.AuctionSearchRequest;
import com.goodsple.features.auction.dto.response.AuctionAdminListResponse;
import com.goodsple.features.auction.entity.Auction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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
    int checkOverlappingAuction(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    // Optional<AuctionAdminDetailResponse> findAuctionDetailById(Long auctionId);
    // void updateAuction(Auction auction);
    // void updateAuctionStatus(@Param("auctionId") Long auctionId, @Param("status") String status);
}
