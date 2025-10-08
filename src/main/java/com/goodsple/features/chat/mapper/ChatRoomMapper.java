package com.goodsple.features.chat.mapper;

import com.goodsple.features.chat.entity.ChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
@Mapper
public interface ChatRoomMapper {
    /**
     * 같은 유저 쌍(+같은 게시글) 방이 이미 있는지 조회
     * - (a,b) 또는 (b,a) 어느 순서든 매칭
     * - postId가 null이면 exchange_post_id IS NULL 과 비교
     * @param a   사용자 A ID
     * @param b   사용자 B ID
     * @param postId 게시글 ID (없으면 null)
     * @return 존재 시 방, 없으면 빈 값
     */
    Optional<ChatRoom> findPair(@Param("a") Long a,
                                @Param("b") Long b,
                                @Param("postId") Long postId);

    /**
     * 채팅방 생성
     * - PostgreSQL 기준: useGeneratedKeys 또는 RETURNING으로 chatRoomId 채움
     * @param room 생성할 방(유저/게시글 정보 세팅 필요)
     * @return 영향 행 수 (1 기대)
     */
    int insert(ChatRoom room); // roomId 생성
}
