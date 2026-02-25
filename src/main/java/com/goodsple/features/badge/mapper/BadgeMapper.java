package com.goodsple.features.badge.mapper;

import com.goodsple.features.badge.domain.Badge;
import com.goodsple.features.badge.domain.UserScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BadgeMapper {

    UserScore findUserScore(Long userId);

    Badge findBadgeByScore(int totalScore);

    Integer findNextMinScore(int totalScore);
}
