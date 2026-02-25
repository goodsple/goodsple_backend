package com.goodsple.features.badge.service;

import com.goodsple.features.badge.domain.Badge;
import com.goodsple.features.badge.domain.UserScore;
import com.goodsple.features.badge.dto.MyScoreResponseDto;
import com.goodsple.features.badge.mapper.BadgeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BadgeService {

    MyScoreResponseDto getMyScore(Long userId);

//    void rewardTradeComplete(Long userId);

    void rewardTradeComplete(Long userId, Long postId, boolean fastResponse);


}
