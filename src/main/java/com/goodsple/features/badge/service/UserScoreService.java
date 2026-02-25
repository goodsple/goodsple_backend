package com.goodsple.features.badge.service;

import com.goodsple.features.badge.mapper.UserScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserScoreService {

    private final UserScoreMapper mapper;

    public void addReviewScore(Long userId, int score) {
        mapper.addReviewScore(userId, score);
    }

    public void addTrustScore(Long userId, int score) {
        mapper.addTrustScore(userId, score);
    }

    public void addPenaltyScore(Long userId, int score) {
        mapper.addPenaltyScore(userId, score);
    }

    public void penaltyReview(Long userId) {

        int score = -10; // 후기 위반 -10점

        mapper.addPenaltyScore(userId, score);
    }

    public void penaltyFakePost(Long userId) {

        int score = -15; // 허위 게시글 -15점

        mapper.addPenaltyScore(userId, score);
    }


}
