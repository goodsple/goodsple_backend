/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/controller/AuctionRealtimeController.java
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuctionRealtimeController {

    private final AuctionRealtimeService auctionRealtimeService;

    @MessageMapping("/auctions/{auctionId}/bid")
    public void handleBid(
            @DestinationVariable Long auctionId,
            @Payload BidRequest bidRequest,
            Principal principal) {

        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        auctionRealtimeService.processBid(auctionId, userDetails, bidRequest);
    }

    @MessageMapping("/auctions/{auctionId}/chat")
    public void handleChat(
            @DestinationVariable Long auctionId,
            @Payload ChatRequest chatRequest,
            Principal principal) {

        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // 서비스가 직접 메시지를 브로드캐스트하도록 호출합니다.
        auctionRealtimeService.broadcastChatMessage(auctionId, userDetails, chatRequest);
    }
}
