package com.goodsple.features.mybids.mapper;

import com.goodsple.features.mybids.dto.response.MyBidsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MyBidsMapper {
    long countWonAuctionsByUserId(@Param("userId") Long userId);

    List<MyBidsResponse> findWonAuctionsByUserId(
            @Param("userId") Long userId,
            @Param("offset") long offset,
            @Param("limit") int limit
    );
}