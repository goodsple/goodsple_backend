package com.goodsple.features.admin.chatbot.mapper;

import com.goodsple.features.admin.chatbot.dto.AdminChatLogDetailResponse;
import com.goodsple.features.admin.chatbot.dto.AdminChatLogResponse;
import com.goodsple.features.admin.chatbot.dto.AdminChatMessageResponse;
import com.goodsple.features.admin.chatbot.dto.AdminChatbotLogDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminChatbotMapper {

    // 로그 목록
    List<AdminChatbotLogDTO> findChatLogs(
            @Param("type") String type,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    // 로그 상세 (요약)
    AdminChatLogDetailResponse findChatLogDetail(
            @Param("logId") Long logId
    );

    // 메시지 목록
    List<AdminChatMessageResponse> findChatMessages(
            @Param("logId") Long logId
    );

    int countChatLogs(@Param("type") String type);
}
