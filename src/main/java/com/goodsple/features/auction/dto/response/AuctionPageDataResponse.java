package com.goodsple.features.auction.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class AuctionPageDataResponse {
    private ItemInfo itemInfo;
    private AuctionStatus status;
    private List<BidHistoryInfo> bidHistory;
    private List<ChatMessageInfo> chatHistory;
    private CurrentUser currentUser;

    @Data public static class ItemInfo { private String id; private String title; private String description; private List<String> images; }
    @Data public static class AuctionStatus { private String auctionStatus; private OffsetDateTime startTime; private String endTime; private java.math.BigDecimal startPrice; private java.math.BigDecimal currentPrice; private String highestBidderNickname; private java.math.BigDecimal minBidUnit; }
    @Data public static class CurrentUser { private String nickname; private boolean isBanned; }
    @Data public static class ChatMessageInfo { private Long chatId; private String userNickname; private String message; private java.time.OffsetDateTime timestamp; }
}
