package com.goodsple.features.prohibitedWord.controller;

import com.goodsple.features.admin.prohibitedWord.service.ProhibitedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prohibited-words")
@RequiredArgsConstructor
public class UserProhibitedWordController {

    private final ProhibitedWordService service;

    @PostMapping("/check")
    public ResponseEntity<?> checkProhibitedWord(@RequestBody String content) {
        List<String> activeWords = service.getActiveWords();
        for (String word : activeWords) {
            if (content.contains(word)) {
                // 예외 던지지 않고 400 Bad Request 반환 + 메시지 포함
                return ResponseEntity
                        .badRequest()
                        .body(word); // 금칙어 단어를 프론트로 전달
            }
        }
        return ResponseEntity.ok().build();
    }
}
