package com.goodsple.features.mybids.service;

import com.goodsple.common.dto.PagedResponse;
import com.goodsple.features.mybids.dto.response.MyBidsResponse;
import com.goodsple.features.mybids.mapper.MyBidsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBidsService {

    private final MyBidsMapper myBidsMapper;

    @Transactional(readOnly = true)
    public PagedResponse<MyBidsResponse> getMyWonAuctions(Long userId, Pageable pageable) {
        long totalElements = myBidsMapper.countWonAuctionsByUserId(userId);
        List<MyBidsResponse> content = myBidsMapper.findWonAuctionsByUserId(
                userId,
                pageable.getOffset(),
                pageable.getPageSize()
        );

        return new PagedResponse<>(
                content,
                pageable.getPageNumber(),
                (int) Math.ceil((double) totalElements / pageable.getPageSize()),
                totalElements
        );
    }
}