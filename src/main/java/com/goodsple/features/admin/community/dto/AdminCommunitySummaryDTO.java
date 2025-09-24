package com.goodsple.features.admin.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommunitySummaryDTO {
    private LocalDate date;            // YYYY-MM-DD
    private String commRoomId;      // 채팅방 ID
    private int participantCount;   // 오늘 참여자 수
    private int messageCount;       // 오늘 메시지 수
    private String lastMessage;     // 마지막 메시지 내용
}
