package com.goodsple.features.community.mapper;

import com.goodsple.features.community.dto.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface CommunityMapper {

    void insertPost(@Param("userId") Long userId,
                    @Param("roomId") String roomId,
                    @Param("content") String content);

    List<Community> selectRecent(@Param("roomId") String roomId,
                                 @Param("limit") int limit,
                                 @Param("before") Instant before);

    Community findUser(@Param("userId") Long userId);
}
