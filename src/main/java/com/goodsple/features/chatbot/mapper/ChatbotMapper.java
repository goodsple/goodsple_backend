package com.goodsple.features.chatbot.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatbotMapper {

    List<String> getAllIntents();

    List<String> findFaqIntents();

    List<String> getQuestionsByIntent(String intent);

    String getAnswerByQuestion(String question);


}
