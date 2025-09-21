/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/ChatMessageResponse.java
 * 설명: 새로운 채팅이 발생했을 때 모든 참여자에게 전파할 데이터를 담는 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class ChatMessageResponse {
    private String type = "CHAT_MESSAGE"; // 메시지 타입
    private String senderNickname;
    private String message;
    private OffsetDateTime timestamp;
}