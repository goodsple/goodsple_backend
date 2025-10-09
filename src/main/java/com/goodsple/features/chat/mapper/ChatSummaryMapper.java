package com.goodsple.features.chat.mapper;

import com.goodsple.features.chat.vo.RoomSummaryRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatSummaryMapper {
    List<RoomSummaryRow> findSummaries(@Param("me") Long me,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);
}
