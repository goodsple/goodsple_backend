/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/controller/AuctionRealtimeController.java
 * 설명: 라이브 경매의 실시간 입찰, 채팅 메시지를 처리하는 WebSocket 컨트롤러입니다.
 */
package com.goodsple.features.auction.controller;

import com.goodsple.features.auction.dto.request.BidRequest;
import com.goodsple.features.auction.dto.request.ChatRequest;
import com.goodsple.features.auction.service.AuctionRealtimeService;
import com.goodsple.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuctionRealtimeController {

    private final AuctionRealtimeService auctionRealtimeService;

    /**
     * 입찰 요청을 처리합니다.
     * 클라이언트는 "/app/auctions/{auctionId}/bid" 주소로 메시지를 보냅니다.
     * @param auctionId 경매 ID
     * @param bidRequest 입찰 정보 DTO
     * @param userDetails 인증된 사용자 정보 (WebSocketConfig의 인터셉터 덕분에 사용 가능)
     */
    @MessageMapping("/auctions/{auctionId}/bid")
    public void handleBid(
            @DestinationVariable Long auctionId,
            @Payload BidRequest bidRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 실제 로직은 서비스 계층에 위임합니다.
        auctionRealtimeService.processBid(auctionId, userDetails, bidRequest);
    }

    /**
     * 실시간 채팅 메시지를 처리합니다.
     * 클라이언트는 "/app/auctions/{auctionId}/chat" 주소로 메시지를 보냅니다.
     */
    @MessageMapping("/auctions/{auctionId}/chat")
    public void handleChat(
            @DestinationVariable Long auctionId,
            @Payload ChatRequest chatRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        auctionRealtimeService.broadcastChatMessage(auctionId, userDetails, chatRequest);
    }
}