/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/request/ChatRequest.java
 * 설명: 사용자가 채팅 메시지 전송 시 보내는 데이터를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.request;

import lombok.Data;

@Data
public class ChatRequest {
    private String message; // 채팅 메시지 내용
}