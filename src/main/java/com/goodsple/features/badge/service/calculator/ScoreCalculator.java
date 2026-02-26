package com.goodsple.features.badge.service.calculator;

import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {

    /* 신뢰도 */
    public int trustFromRating(int rating) {
        return rating; // 1~5 그대로
    }

    /* 후기 */
    public int reviewTextScore(String content) {
        return content.length() >= 50 ? 1 : 0;
    }

    public int reviewImageScore(int imageCount) {
        if (imageCount >= 4) return 5;
        if (imageCount == 3) return 4;
        if (imageCount == 2) return 3;
        if (imageCount == 1) return 2;
        return 0;
    }

    /* 거래 */
    public int tradeCompleteScore() {
        return 5;
    }

    public int monthlyBonus(int monthlyCount) {
        return monthlyCount >= 3 ? 5 : 0;
    }

    public int fastResponseScore(boolean fast) {
        return fast ? 3 : 0;
    }

    /* 감점 */
    public int violationReview() {
        return -10;
    }

    public int violationFakePost() {
        return -15;
    }

    /* 리뷰 총점 계산 (ReviewService에서 사용하는 메서드) */
    public int calculateReviewScore(String content, int imageCount) {
        return reviewTextScore(content) + reviewImageScore(imageCount);
    }

}
