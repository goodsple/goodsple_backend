package com.goodsple.features.chat.service;

import com.goodsple.features.chat.dto.RoomSummaryRes;
import com.goodsple.features.chat.entity.ChatMessage;

import java.util.List;
import java.util.Map;

/**
 * 채팅 서비스 인터페이스
 * - 방 생성/재사용
 * - 메시지 조회/전송
 * - 읽음 커서 갱신 및 조회
 */
public interface ChatService {

    /**
     * 방 생성 또는 기존 방 재사용
     * @param me     현재 사용자 ID
     * @param peer   상대 사용자 ID
     * @param postId 게시글 ID (null 가능)
     * @return 생성/재사용된 방 ID
     */
    Long createOrGetRoom(Long me, Long peer, Long postId);

    /**
     * 메시지 페이지 조회 (최신부터 위로)
     * 키셋 페이지네이션: WHERE message_id < :beforeId ORDER BY message_id DESC LIMIT :limit
     * @param roomId   방 ID
     * @param beforeId 기준 메시지 ID(미만 조회, null이면 최신부터)
     * @param limit    최대 개수(1~100 권장)
     * @return 최신 → 과거 순 메시지 목록
     */
    List<ChatMessage> load(Long roomId, Long beforeId, int limit);

    /**
     * 메시지 전송
     * @param me     현재 사용자 ID(보내는 사람)
     * @param roomId 방 ID
     * @param text   본문(1~1000자 권장)
     * @return 저장된 메시지(생성된 messageId/createdAt 포함)
     */
    ChatMessage sendMessage(Long me, Long roomId, String text);

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

    /**
     * 내 채팅방 요약 리스트
     * @param me     현재 사용자 ID
     * @param limit  개수
     * @param offset 시작 오프셋
     */
    List<RoomSummaryRes> listSummaries(Long me, int limit, int offset);

    Long findPeerId(Long roomId, Long me);

}
