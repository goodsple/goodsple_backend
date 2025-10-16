package com.goodsple.features.admin.chatbot.controller;

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
}
