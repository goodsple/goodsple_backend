package com.goodsple.features.admin.community.mapper;

import com.goodsple.features.admin.community.dto.AdminCommunitySummaryDTO;
import com.goodsple.features.admin.community.dto.AdminCommunityDetailDTO;
import com.goodsple.features.admin.community.dto.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AdminCommunityMapper {

    // 요약 테이블 (하루+채팅방 단위)
    List<AdminCommunitySummaryDTO> findDailySummary();

    // 상세보기 (특정 날짜 + 채팅방)
    List<AdminCommunityDetailDTO> findDetailsByDateAndRoom(
            @Param("date") LocalDate date,
            @Param("commRoomId") String commRoomId
    );

    List<ChatRoomDTO> findAllChatRooms();

    // 필터 적용된 요약 조회
    List<AdminCommunitySummaryDTO> findDailySummaryFiltered(
            @Param("roomId") String roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



}
