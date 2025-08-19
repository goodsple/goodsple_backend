package com.goodsple.features.community.service;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper mapper;
    private final RoomValidator roomValidator;

    // 게시글 저장
    public void savePost(String roomId, Long userId, String content) {
        roomValidator.ensureValid(roomId);
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용이 비어 있습니다.");
        }
        mapper.insertPost(userId, roomId, content, Instant.now());
    }

    // 게시글 조회 (최신 → 오래된 순)
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
}
