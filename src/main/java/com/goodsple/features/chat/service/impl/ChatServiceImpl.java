package com.goodsple.features.chat.service.impl;

import com.goodsple.features.chat.entity.ChatMessage;
import com.goodsple.features.chat.entity.ChatParticipant;
import com.goodsple.features.chat.entity.ChatRoom;
import com.goodsple.features.chat.mapper.ChatMessageMapper;
import com.goodsple.features.chat.mapper.ChatParticipantMapper;
import com.goodsple.features.chat.mapper.ChatRoomMapper;
import com.goodsple.features.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final ChatRoomMapper roomMapper;
    private final ChatMessageMapper messageMapper;
    private final ChatParticipantMapper participantMapper;

    /**
     * 방 생성 또는 기존 방 재사용
     *
     * @param me     현재 사용자 ID
     * @param peer   상대 사용자 ID
     * @param postId 게시글 ID (없으면 null)
     * @return 생성/재사용된 chatRoomId
     *
     * 처리 흐름
     *  1) (me, peer, postId) 조합으로 기존 방 조회
     *  2) 없으면 새로 INSERT (PK 생성)
     *  3) 참가자 테이블에 (roomId, me), (roomId, peer) 를 멱등 upsert
     */

    @Override
    public Long createOrGetRoom(Long me, Long peer, Long postId) {
        ChatRoom room = roomMapper.findPair(me, peer, postId)
                .orElseGet(() -> {
                    ChatRoom r = ChatRoom.builder()
                            .exchangePostId(postId)
                            .user1Id(me)
                            .user2Id(peer)
                            .build();
                    // INSERT 시 useGeneratedKeys=true (XML)로 chatRoomId 세팅됨
                    roomMapper.insert(r);
                    return r;
                });

        // 참가자 멱등 등록 (이미 있으면 아무 작업 안 함)
        participantMapper.upsert(room.getChatRoomId(), me);
        participantMapper.upsert(room.getChatRoomId(), peer);

        return room.getChatRoomId();
    }

    /**
     * 메시지 페이지 조회 (최신부터 위로)
     *
     * @param roomId   방 ID
     * @param beforeId 기준 메시지 ID (해당 ID보다 작은 것만 조회 / null이면 최신부터)
     * @param limit    최대 개수 (안전 범위로 클램프)
     * @return 메시지 리스트 (내림차순: 최신 → 과거)
     *
     * 키셋 페이지네이션:
     *  - WHERE message_id < :beforeId
     *  - ORDER BY message_id DESC
     *  - LIMIT :limit
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> load(Long roomId, Long beforeId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        return messageMapper.page(roomId, beforeId, safeLimit);
    }

    /**
     * 메시지 저장
     *
     * @param roomId   방 ID
     * @param senderId 보낸 사람 ID(=현재 사용자)
     * @param content  메시지 본문
     * @return 저장된 메시지(자동 생성된 messageId 포함)
     *
     * 주의:
     *  - 방 참여자 검증(권한 체크)은 컨트롤러/필터 레벨에서 선행 권장
     */
    @Override
    public ChatMessage send(Long roomId, Long senderId, String content) {
        ChatMessage m = ChatMessage.builder()
                .chatRoomId(roomId)
                .senderId(senderId)
                .message(content)
                .build();
        // INSERT 시 useGeneratedKeys=true (XML)로 messageId 세팅됨
        messageMapper.insert(m);
        return m;
    }

    /**
     * 읽음 커서 갱신
     *
     * @param roomId 방 ID
     * @param userId 사용자 ID(읽은 사람)
     * @param lastId 마지막으로 본 메시지 ID
     *
     * 규칙:
     *  - 커서는 "앞으로만" 이동 (GREATEST(현재, 전달값))
     *  - 리스트 하단까지 봤을 때만 lastId를 보내도록 클라이언트에서 제어 권장
     */
    @Override
    public void read(Long roomId, Long userId, Long lastId) {
        participantMapper.bumpCursor(roomId, userId, lastId);
    }

    /**
     * 내/상대 읽음 커서 조회
     *
     * @param roomId 방 ID
     * @param me     현재 사용자 ID
     * @return Map { "myLastReadMessageId": Long, "peerLastReadMessageId": Long }
     *
     * 프론트 사용처:
     *  - 내 마지막 보낸 메시지 ID ≤ 상대 커서 → "읽음" 표시
     *  - 안읽음 구분선 위치 계산 등에 활용
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> cursors(Long roomId, Long me) {
        Long myCursor = participantMapper.findOne(roomId, me)
                .map(ChatParticipant::getLastReadMessageId)
                .orElse(null);
        Long peerCursor = participantMapper.findPeer(roomId, me)
                .map(ChatParticipant::getLastReadMessageId)
                .orElse(null);

        // 프론트 단의 편의를 위해 null 대신 0L 반환도 가능 (여기서는 null→0 치환)
        return Map.of(
                "myLastReadMessageId",  (myCursor  == null ? 0L : myCursor),
                "peerLastReadMessageId",(peerCursor== null ? 0L : peerCursor)
        );
    }
}
