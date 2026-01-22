package com.goodsple.features.chatbot.mapper;

import com.goodsple.features.chatbot.dto.ChatbotLog;
import com.goodsple.features.chatbot.dto.ChatbotMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatbotMapper {

    List<String> getAllIntents();

    List<String> findFaqIntents();

    List<String> getQuestionsByIntent(String intent);

    String getAnswerByQuestion(String question);


    // 대화 로그 생성
    void insertChatLog(ChatbotLog log);

    // 메시지 저장
    void insertChatMessage(ChatbotMessage msg);

    // 가장 최근 로그 ID (세션 기반)
    Long findLatestLogIdBySession(String sessionId);


    int findNextMessageOrder(Long logId);


}
