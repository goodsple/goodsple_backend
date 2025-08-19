package com.goodsple.features.community.service;

import com.goodsple.features.community.dto.Community;
import com.goodsple.features.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper mapper;
    private final RoomValidator roomValidator;

    // 게시글 저장
    public void savePost(String roomId, Long userId, String content) {
        roomValidator.ensureValid(roomId);
        if (content == null || content.isBlank()) throw new IllegalArgumentException("Empty content");
        mapper.insertPost(userId, roomId, content);
    }

    // 게시글 조회 (오래된 → 최신 순)
    public List<Community> getPosts(String roomId, int limit, Instant before) {
        roomValidator.ensureValid(roomId);
        List<Community> rows = mapper.selectRecent(roomId, limit, before);
        Collections.reverse(rows);
        return rows;
    }

    // 유저 정보 조회
    public Community getUserInfo(Long userId) {
        return mapper.findUser(userId);
    }
}
