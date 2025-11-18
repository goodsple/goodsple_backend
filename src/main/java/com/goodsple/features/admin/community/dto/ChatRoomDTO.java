package com.goodsple.features.admin.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private String roomId;
    private String roomName; // 필요 시 채팅방 이름 필드
}