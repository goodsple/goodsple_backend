package com.goodsple.features.admin.prohibitedWord.dto;

// 금칙어 추가 요청
public class ProhibitedWordRequest {
    private String word; // final 제거

    // 기본 생성자 추가 (Spring이 JSON 매핑할 때 필요)
    public ProhibitedWordRequest() {}

    // 기존 생성자
    public ProhibitedWordRequest(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
