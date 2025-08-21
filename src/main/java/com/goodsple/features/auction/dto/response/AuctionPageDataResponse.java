/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/AuctionPageDataResponse.java
 * 설명: 라이브 경매 페이지 전체 데이터를 담는 메인 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class AuctionPageDataResponse {
    private ItemInfo itemInfo;
    private AuctionStatus status;
    private List<BidHistoryInfo> bidHistory;
    private List<ChatMessageInfo> chatHistory;
    private CurrentUser currentUser;

    // MyBatis가 중첩된 객체를 채울 수 있도록 내부 클래스로 정의
    @Data public static class ItemInfo { private String id; private String title; private String description; private List<String> images; }
    @Data public static class AuctionStatus { private String startTime; private String endTime; private java.math.BigDecimal startPrice; private java.math.BigDecimal currentPrice; private String highestBidderNickname; private java.math.BigDecimal minBidUnit; }
    @Data public static class CurrentUser { private String nickname; private boolean isBanned; }
    @Data public static class ChatMessageInfo { private Long chatId; private String userNickname; private String message; private java.time.OffsetDateTime timestamp; }
}
