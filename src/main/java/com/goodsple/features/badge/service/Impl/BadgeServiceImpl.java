package com.goodsple.features.badge.service.Impl;

import com.goodsple.features.badge.domain.Badge;
import com.goodsple.features.badge.domain.UserScore;
import com.goodsple.features.badge.dto.MyScoreResponseDto;
import com.goodsple.features.badge.mapper.BadgeMapper;
import com.goodsple.features.badge.mapper.UserScoreMapper;
import com.goodsple.features.badge.service.BadgeService;
import com.goodsple.features.badge.service.calculator.ScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private final BadgeMapper badgeMapper;
    private final UserScoreMapper userScoreMapper;
    private final ScoreCalculator scoreCalculator;

    @Override
    public MyScoreResponseDto getMyScore(Long userId) {

        UserScore score = badgeMapper.findUserScore(userId);

        if (score == null) {
            score = new UserScore();
        }

        int totalScore =
                score.getTrustScore()
                        + score.getReviewScore()
                        + score.getTradeScore()
                        - score.getPenaltyScore();

        Badge badge = badgeMapper.findBadgeByScore(totalScore);

        Integer nextMin = badgeMapper.findNextMinScore(totalScore);

        return MyScoreResponseDto.builder()
                .userId(userId)
                .trustScore(score.getTrustScore())
                .reviewScore(score.getReviewScore())
                .penaltyScore(score.getPenaltyScore())
                .totalScore(totalScore)
                .badgeLevel(badge.getBadgeLevel())
                .badgeName(badge.getBadgeName())
                .badgeImageUrl(badge.getBadgeImageUrl())
                .nextMinScore(nextMin)
                .scoreGap(nextMin == null ? 0 : nextMin - totalScore)
                .build();
    }



    @Override
    public void rewardTradeComplete(Long userId, Long postId, boolean fastResponse) {

        int score = scoreCalculator.tradeCompleteScore(); // +5
        int monthlyCount = userScoreMapper.countMonthlyTrades(userId);

        if (monthlyCount >= 3) {
            int exists = userScoreMapper.existsMonthlyBonus(userId);
            if (exists == 0) {
                score += scoreCalculator.monthlyBonus(monthlyCount); // +5
                userScoreMapper.addMonthlyBonusHistory(userId);
            }
        }

        if (fastResponse) {
            score += scoreCalculator.fastResponseScore(true); // +3
        }

        userScoreMapper.addTradeScoreWithHistory(userId, score, postId);

    }

}
