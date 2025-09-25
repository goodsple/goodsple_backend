package com.goodsple.features.admin.community.controller;

import com.goodsple.features.admin.community.dto.AdminCommunityDetailDTO;
import com.goodsple.features.admin.community.dto.AdminCommunitySummaryDTO;
import com.goodsple.features.admin.community.dto.ChatRoomDTO;
import com.goodsple.features.admin.community.service.AdminCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/community")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final AdminCommunityService adminCommunityService;

    @GetMapping("/summary")
    public List<AdminCommunitySummaryDTO> getDailySummaries(
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        // 둘 다 없으면 전체
        if ((roomId == null || roomId.isBlank()) && startDate == null && endDate == null) {
            return adminCommunityService.getDailySummaries();
        }

        // 둘 다 있을 때는 기존 필터 메서드 사용
        return adminCommunityService.getDailySummariesFiltered(roomId, startDate, endDate);
    }


    // 상세보기
    @GetMapping("/details")
    public List<AdminCommunityDetailDTO> getDetails(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String commRoomId
    ) {
        return adminCommunityService.getDetails(date, commRoomId);
    }

    // 채팅방 목록
    @GetMapping("/rooms")
    public List<ChatRoomDTO> getChatRooms() {
        return adminCommunityService.getAllChatRooms();
    }






}
