package com.goodsple.features.admin.community.service;

import com.goodsple.features.admin.community.dto.AdminCommunityDetailDTO;
import com.goodsple.features.admin.community.dto.AdminCommunitySummaryDTO;
import com.goodsple.features.admin.community.dto.ChatRoomDTO;
import com.goodsple.features.admin.community.mapper.AdminCommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommunityService {

    private final AdminCommunityMapper adminCommunityMapper;

    public List<AdminCommunitySummaryDTO> getDailySummaries() {
        return adminCommunityMapper.findDailySummary();
    }

    public List<AdminCommunityDetailDTO> getDetails(LocalDate date, String commRoomId) {
        return adminCommunityMapper.findDetailsByDateAndRoom(date, commRoomId);
    }

    public List<ChatRoomDTO> getAllChatRooms() {
        return adminCommunityMapper.findAllChatRooms();
    }


    public List<AdminCommunitySummaryDTO> getDailySummariesFiltered(String roomId, LocalDate startDate, LocalDate endDate) {
        if (roomId != null && roomId.isBlank()) {
            roomId = null;
        }
        return adminCommunityMapper.findDailySummaryFiltered(roomId, startDate, endDate);
    }


}
