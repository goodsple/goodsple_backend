package com.goodsple.features.admin.prohibitedWord.controller;

import com.goodsple.features.admin.prohibitedWord.dto.ProhibitedWordDTO;
import com.goodsple.features.admin.prohibitedWord.dto.ProhibitedWordRequest;
import com.goodsple.features.admin.prohibitedWord.service.ProhibitedWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/prohibited-words")
public class ProhibitedWordController {

    private final ProhibitedWordService service;

    public ProhibitedWordController(ProhibitedWordService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProhibitedWordDTO>> getAllWords() {
        return ResponseEntity.ok(service.getAllWords());
    }

    @PostMapping
    public ResponseEntity<Void> addWord(@RequestBody ProhibitedWordRequest request) {
        service.addWord(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWords(@RequestBody List<Long> ids) {
        service.deleteWords(ids);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleWord(@PathVariable Long id) {
        service.toggleWord(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/check")
//    public ResponseEntity<?> checkProhibitedWord(@RequestBody String content) {
//        List<String> activeWords = service.getActiveWords();
//        for (String word : activeWords) {
//            if (content.contains(word)) {
//                // 예외 던지지 않고 400 Bad Request 반환 + 메시지 포함
//                return ResponseEntity
//                        .badRequest()
//                        .body(word); // 금칙어 단어를 프론트로 전달
//            }
//        }
//        return ResponseEntity.ok().build();
//    }



}