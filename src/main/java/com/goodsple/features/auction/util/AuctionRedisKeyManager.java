/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/util/AuctionRedisKeyManager.java
 * 설명: 라이브 경매에 사용되는 Redis 키를 생성하고 관리하는 유틸리티 클래스입니다.
 */
package com.goodsple.features.auction.util;

import org.springframework.stereotype.Component;

@Component
public class AuctionRedisKeyManager {

    private static final String AUCTION_STATE_KEY_PREFIX = "auction:state:";
    private static final String AUCTION_BIDS_KEY_PREFIX = "auction:bids:";

    /**
     * 경매 상태 정보가 저장된 Hash의 키를 반환합니다.
     * 예: "auction:state:123"
     */
    public String getAuctionStateKey(Long auctionId) {
        return AUCTION_STATE_KEY_PREFIX + auctionId;
    }

    /**
     * 경매 입찰 내역이 저장된 Sorted Set의 키를 반환합니다.
     * 예: "auction:bids:123"
     */
    public String getAuctionBidsKey(Long auctionId) {
        return AUCTION_BIDS_KEY_PREFIX + auctionId;
    }
}