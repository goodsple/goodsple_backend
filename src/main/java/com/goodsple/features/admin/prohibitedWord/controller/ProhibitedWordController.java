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
}