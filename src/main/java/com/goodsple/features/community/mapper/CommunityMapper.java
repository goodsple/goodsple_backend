package com.goodsple.features.community.mapper;

import com.goodsple.features.community.dto.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface CommunityMapper {

    // 게시글 삽입
    void insertPost(@Param("userId") Long userId,
                    @Param("roomId") String roomId,
                    @Param("content") String content,
                    @Param("createdAt") Instant createdAt);

    // 특정 roomId의 게시글 조회 (createdAt 기준 내림차순)
    List<Community> selectRecent(@Param("roomId") String roomId,
                                 @Param("limit") int limit,
                                 @Param("before") Instant before);

    // 유저 정보 조회
    Community findUser(@Param("userId") Long userId);
}
