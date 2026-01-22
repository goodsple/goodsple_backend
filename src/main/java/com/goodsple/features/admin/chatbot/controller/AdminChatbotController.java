package com.goodsple.features.admin.chatbot.controller;

import com.goodsple.features.admin.chatbot.dto.AdminChatLogDetailResponse;
import com.goodsple.features.admin.chatbot.dto.AdminChatLogResponse;
import com.goodsple.features.admin.chatbot.dto.AdminChatbotLogDTO;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseCreateRequest;
import com.goodsple.features.admin.chatbot.dto.request.KnowledgeBaseUpdateRequest;
import com.goodsple.features.admin.chatbot.dto.response.KnowledgeBaseResponse;
import com.goodsple.features.admin.chatbot.service.AdminChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/chatbot")
@RequiredArgsConstructor
public class AdminChatbotController {

    private final AdminChatbotService chatbotService;

    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeBaseResponse>> getKnowledgeList() {
        return ResponseEntity.ok(chatbotService.getKnowledgeList());
    }

    @PostMapping("/knowledge")
    public ResponseEntity<KnowledgeBaseResponse> createKnowledge(@RequestBody KnowledgeBaseCreateRequest request) {
        return ResponseEntity.ok(chatbotService.createKnowledge(request));
    }

    @PutMapping("/knowledge/{id}")
    public ResponseEntity<KnowledgeBaseResponse> updateKnowledge(@PathVariable Long id, @RequestBody KnowledgeBaseUpdateRequest request) {
        return ResponseEntity.ok(chatbotService.updateKnowledge(id, request));
    }

    @DeleteMapping("/knowledge/{id}")
    public ResponseEntity<KnowledgeBaseResponse> deleteKnowledge(@PathVariable Long id) {
        return ResponseEntity.ok(chatbotService.deleteKnowledge(id));
    }

    // ------------------------

    // 챗봇 로그 목록 (FAQ / QNA)
    @GetMapping("/logs")
    public List<AdminChatbotLogDTO> getChatLogs(
            @RequestParam String type,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return chatbotService.getChatLogs(type, page, size);
    }

    // 챗봇 로그 상세 (대화 내역)
    @GetMapping("/logs/{logId}")
    public AdminChatLogDetailResponse getChatLogDetail(
            @PathVariable Long logId
    ) {
        return chatbotService.getChatLogDetail(logId);
    }























}
