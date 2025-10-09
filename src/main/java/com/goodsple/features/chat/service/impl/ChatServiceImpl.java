package com.goodsple.features.chat.service.impl;

import com.goodsple.features.chat.dto.RoomSummaryRes;
import com.goodsple.features.chat.entity.ChatMessage;
import com.goodsple.features.chat.entity.ChatParticipant;
import com.goodsple.features.chat.entity.ChatRoom;
import com.goodsple.features.chat.mapper.ChatMessageMapper;
import com.goodsple.features.chat.mapper.ChatParticipantMapper;
import com.goodsple.features.chat.mapper.ChatRoomMapper;
import com.goodsple.features.chat.mapper.ChatSummaryMapper;
import com.goodsple.features.chat.service.ChatService;
import com.goodsple.features.chat.vo.RoomSummaryRow;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final ChatRoomMapper roomMapper;
    private final ChatMessageMapper messageMapper;
    private final ChatParticipantMapper participantMapper;
    private final ChatSummaryMapper summaryMapper;

    @Override
    public Long createOrGetRoom(Long me, Long peer, Long postId) {
        ChatRoom room = roomMapper.findPair(me, peer, postId)
                .orElseGet(() -> {
                    ChatRoom r = ChatRoom.builder()
                            .exchangePostId(postId)
                            .user1Id(me)
                            .user2Id(peer)
                            .build();
                    roomMapper.insert(r); // useGeneratedKeys=true 또는 RETURNING
                    return r;
                });
        participantMapper.upsert(room.getChatRoomId(), me);
        participantMapper.upsert(room.getChatRoomId(), peer);
        return room.getChatRoomId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> load(Long roomId, Long beforeId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        return messageMapper.page(roomId, beforeId, safeLimit);
    }

    // 컨트롤러와 맞춘 “유일한” 전송 메서드
    @Override
    public ChatMessage sendMessage(Long me, Long roomId, String text) {
        if (roomId == null) throw new IllegalArgumentException("roomId required");
        if (text == null || text.trim().isEmpty()) throw new IllegalArgumentException("text required");
        String normalized = text.trim();
        if (normalized.length() > 1000) throw new IllegalArgumentException("text too long");

        boolean isParticipant = participantMapper.findOne(roomId, me).isPresent();
        if (!isParticipant) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 방의 참여자가 아닙니다.");
        }

        ChatMessage m = ChatMessage.builder()
                .chatRoomId(roomId)
                .senderId(me)
                .message(normalized)
                .build();
        messageMapper.insert(m); // 생성 PK/시간 세팅 (XML에서 처리)
        return m;
    }

    @Override
    public void read(Long roomId, Long userId, Long lastId) {
        participantMapper.bumpCursor(roomId, userId, lastId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> cursors(Long roomId, Long me) {
        Long myCursor = participantMapper.findOne(roomId, me)
                .map(ChatParticipant::getLastReadMessageId).orElse(0L);
        Long peerCursor = participantMapper.findPeer(roomId, me)
                .map(ChatParticipant::getLastReadMessageId).orElse(0L);
        return Map.of("myLastReadMessageId", myCursor, "peerLastReadMessageId", peerCursor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomSummaryRes> listSummaries(Long me, int limit, int offset) {
        List<RoomSummaryRow> rows = summaryMapper.findSummaries(me, limit, offset);
        return rows.stream().map(r -> {
            RoomSummaryRes.Peer peer = new RoomSummaryRes.Peer(
                    r.getPeerUserId(), r.getPeerNickname(), r.getPeerAvatar(),
                    r.getPeerVerified(), r.getPeerLevelText()
            );
            RoomSummaryRes.LastMessage last = (r.getLastMessageId() == null) ? null
                    : new RoomSummaryRes.LastMessage(
                    r.getLastMessageId(), r.getLastText(),
                    r.getLastCreatedAt() == null ? null : r.getLastCreatedAt().toString()
            );
            return new RoomSummaryRes(
                    r.getRoomId(), peer, last,
                    r.getUnreadCount() == null ? 0 : r.getUnreadCount(),
                    r.getUpdatedAt() == null ? null : r.getUpdatedAt().toString(),
                    null
            );
        }).toList();
    }
}
