package com.goodsple.features.badge.mapper;

import com.goodsple.features.badge.domain.Badge;
import com.goodsple.features.badge.domain.UserScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserScoreMapper {

    void addReviewScore(Long userId, int score);

    void addTrustScore(Long userId, int score);

    void addPenaltyScore(Long userId, int score);

    // 거래 1건 완료 시 +5점
    void addTradeScore(
            @Param("userId") Long userId,
            @Param("score") int score
    );

    // 한달 내 3건 이상 거래 시 +3점
    int countMonthlyTrades(Long userId);

    void addTradeScoreWithHistory(
            @Param("userId") Long userId,
            @Param("score") int score,
            @Param("postId") Long postId
    );

}
