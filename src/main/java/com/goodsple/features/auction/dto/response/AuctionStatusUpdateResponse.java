package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class AuctionStatusUpdateResponse {
    private String type = "AUCTION_UPDATE";
    private BigDecimal currentPrice;
    private String topBidderNickname;
    private OffsetDateTime extendedEndTime;
    private BidHistoryInfo newBid;
}