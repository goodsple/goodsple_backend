package com.goodsple.features.chat.mapper;

import com.goodsple.features.chat.entity.ChatParticipant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ChatParticipantMapper {
    /**
     * 참가자 행 멱등 생성
     * - (roomId, userId) PK로 존재하면 아무 일도 하지 않음
     * - PostgreSQL: ON CONFLICT (chat_room_id, user_id) DO NOTHING
     */
    int upsert(@Param("roomId") Long roomId, @Param("userId") Long userId);

    /**
     * 내 참가자 레코드 조회
     */
    Optional<ChatParticipant> findOne(@Param("roomId") Long roomId,
                                      @Param("userId") Long userId);

    /**
     * 상대 참가자 레코드 조회 (같은 방에서 me가 아닌 사람 1명)
     */
    Optional<ChatParticipant> findPeer(@Param("roomId") Long roomId,
                                       @Param("me") Long me);

    /**
     * 읽음 커서 갱신 (앞으로만 전진)
     * - last_read_message_id = GREATEST(current, :lastId)
     */
    int bumpCursor(@Param("roomId") Long roomId,
                   @Param("userId") Long userId,
                   @Param("lastId") Long lastId);
}
