package com.goodsple.features.chat.mapper;

import com.goodsple.features.chat.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    /**
     * 메시지 저장
     * - 키 자동생성 사용 (PostgreSQL 시퀀스)
     * - XML에 useGeneratedKeys=true, keyProperty="messageId" 설정
     */
    int insert(ChatMessage m);

    /**
     * 메시지 페이지 조회 (최신부터 위로)
     * - beforeId가 있으면 그 ID보다 작은 메시지만 가져옴(키셋 페이지)
     * - ORDER BY message_id DESC LIMIT :limit
     */
    List<ChatMessage> page(@Param("roomId") Long roomId,
                           @Param("beforeId") Long beforeId,
                           @Param("limit") int limit);

    /**
     * 방의 마지막 메시지 ID 가져오기
     * - 읽음 커서 초기화/검증 등에 사용 가능
     */
    Long lastId(@Param("roomId") Long roomId);
}
