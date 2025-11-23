package com.goodsple.features.chatbot.controller;


import com.goodsple.features.chatbot.dto.request.ChatbotRequest;
import com.goodsple.features.chatbot.dto.response.ChatbotResponse;
import com.goodsple.features.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatService;

    @PostMapping("/chatbot/messages")
    public ChatbotResponse chat(@RequestBody ChatbotRequest req) {
        return chatService.handle(req);
    }

//    @GetMapping("/intents")
//    public List<String> getFaqIntents() {
//        return chatService.getFaqIntents();
//    }

    // FAQ Intent 목록 조회
    @GetMapping("/intents")
    public ResponseEntity<List<String>> getFaqIntents() {
        List<String> intents = chatService.getFaqIntents();
        return ResponseEntity.ok(intents);
    }



    @GetMapping("/questions")
    public List<String> getQuestionsByIntent(@RequestParam String intent) {
        return chatService.getQuestionsByIntent(intent);
    }

    @GetMapping("/answer")
    public Map<String, String> getAnswerByQuestion(@RequestParam String question) {
        String answer = chatService.getAnswerByQuestion(question);

        Map<String, String> response = new HashMap<>();
        response.put("answer", answer != null ? answer : "등록된 답변이 없습니다.");
        return response;
    }

}
