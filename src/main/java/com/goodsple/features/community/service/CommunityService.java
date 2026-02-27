package com.goodsple.features.community.service;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper mapper;
    private final RoomValidator roomValidator;
    private final SimpMessagingTemplate template;

    // 방별 접속 세션 관리
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();

    // 한 달 확성기 사용 제한 => 2번
    private static final int MEGAPHONE_LIMIT_PER_MONTH = 2;

    // 채팅 저장
    public void savePost(String roomId, Long userId, String content, String type) {
        roomValidator.ensureValid(roomId);

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용이 비어 있습니다.");
        }

        if (type == null || (!type.equals("GENERAL") && !type.equals("MEGAPHONE"))) {
            throw new IllegalArgumentException("유효하지 않은 type: " + type);
        }

        // MEGAPHONE인 경우 한 달 제한 체크
        if (type.equals("MEGAPHONE")) {
            int usedCount = mapper.countMegaphoneThisMonth(userId, LocalDate.now());
            if (usedCount >= MEGAPHONE_LIMIT_PER_MONTH) {
                throw new IllegalStateException("이번 달 확성기 사용 횟수를 모두 사용했습니다.");
            }
        }

        mapper.insertPost(userId, roomId, type, content, Instant.now());
    }

    // 채팅 조회 (최신 → 오래된 순)
    public List<Community> getPosts(String roomId, int limit, Instant before) {
        roomValidator.ensureValid(roomId);
        // before가 null이면 XML에서 조건 처리
        return mapper.selectRecent(roomId, limit, before);
    }

    // 유저 정보 조회
    public Community getUserInfo(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        return mapper.findUser(userId);
    }


    // --- 방 참여/퇴장 처리 ---
    public void joinRoom(String roomId, String sessionId) {
        roomValidator.ensureValid(roomId);
        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
        broadcastOnlineCount(roomId);
    }

    public void leaveRoom(String roomId, String sessionId) {
        roomValidator.ensureValid(roomId);
        Set<String> sessions = roomUsers.get(roomId);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) roomUsers.remove(roomId);
            broadcastOnlineCount(roomId);
        }
    }

    // 모든 방 ID 조회
    public Set<String> getAllRooms() {
        return roomUsers.keySet();
    }

    // 접속자 수 조회
    public int getRoomUserCount(String roomId) {
        return roomUsers.getOrDefault(roomId, Set.of()).size();
    }

    private void broadcastOnlineCount(String roomId) {
        int count = getRoomUserCount(roomId);
        template.convertAndSend("/topic/roomUsers/" + roomId, count);
    }


    // 확성기 남은 횟수 조회 메서드
    public int getMegaphoneRemaining(Long userId) {
        int usedCount = mapper.countMegaphoneThisMonth(userId, LocalDate.now());
        return Math.max(0, MEGAPHONE_LIMIT_PER_MONTH - usedCount);
    }

    // 최신 확성기 메시지 1개 반환 메서드 ( 메인페이지 )
    public Community getLatestMegaphone() {
        List<Community> list = mapper.selectLatestMegaphone(1); // 최신 1개
        return list.isEmpty() ? null : list.get(0);
    }

    public void saveJoinLog(String roomId, Long userId) {
        mapper.insertJoinLog(userId, roomId, Instant.now());
    }


}
