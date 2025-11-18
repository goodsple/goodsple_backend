package com.goodsple.features.chatbot.controller;


import com.goodsple.features.chatbot.dto.request.ChatbotRequest;
import com.goodsple.features.chatbot.dto.response.ChatbotResponse;
import com.goodsple.features.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatService;

    @PostMapping("/chatbot/messages")
    public ChatbotResponse chat(@RequestBody ChatbotRequest req) {
        return chatService.handle(req);
    }
}
