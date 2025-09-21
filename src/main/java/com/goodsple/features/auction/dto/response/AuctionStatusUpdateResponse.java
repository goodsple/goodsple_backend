/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/AuctionStatusUpdateResponse.java
 * 설명: 입찰 발생 등 경매 상태가 변경될 때 모든 참여자에게 전파(Broadcast)할 데이터를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import com.goodsple.features.auction.dto.response.BidHistoryInfo; // import 문 추가
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class AuctionStatusUpdateResponse {
    private String type = "AUCTION_UPDATE"; // 메시지 타입 (프론트엔드 식별용)
    private BigDecimal currentPrice;
    private String topBidderNickname;
    private OffsetDateTime extendedEndTime; // 시간 연장 시 변경된 종료 시간
    private BidHistoryInfo newBid; // 새로 추가된 입찰 내역
}