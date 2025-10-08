package com.goodsple.features.chat.service;

import com.goodsple.features.chat.entity.ChatMessage;

import java.util.List;
import java.util.Map;

/**
 * 채팅 서비스 인터페이스
 * - 방 생성/재사용
 * - 메시지 조회/저장
 * - 읽음 커서 갱신 및 조회
 */
public interface ChatService {

    /**
     * 방 생성 또는 기존 방 재사용
     * @param me     현재 사용자 ID
     * @param peer   상대 사용자 ID
     * @param postId 게시글 ID(null 가능)
     * @return 생성/재사용된 방 ID
     */
    Long createOrGetRoom(Long me, Long peer, Long postId);

    /**
     * 메시지 페이지 조회 (최신부터 위로)
     * @param roomId   방 ID
     * @param beforeId 기준 메시지 ID(미만 조회, null이면 최신부터)
     * @param limit    최대 개수(1~100 권장)
     */
    List<ChatMessage> load(Long roomId, Long beforeId, int limit);

    /**
     * 메시지 저장
     * @param roomId   방 ID
     * @param senderId 보낸 사람 ID
     * @param content  본문
     * @return 저장된 메시지(생성된 messageId 포함)
     */
    ChatMessage send(Long roomId, Long senderId, String content);

    /**
     * 읽음 커서 갱신(앞으로만 전진)
     * @param roomId 방 ID
     * @param userId 사용자 ID
     * @param lastId 마지막으로 본 메시지 ID
     */
    void read(Long roomId, Long userId, Long lastId);

    /**
     * 내/상대 읽음 커서 조회
     * @param roomId 방 ID
     * @param me     현재 사용자 ID
     * @return { "myLastReadMessageId": Long, "peerLastReadMessageId": Long }
     */
    Map<String, Long> cursors(Long roomId, Long me);
}
