package com.goodsple.features.chatbot.service;


import com.goodsple.features.chatbot.dto.ChatbotLog;
import com.goodsple.features.chatbot.dto.ChatbotMessage;
import com.goodsple.features.chatbot.dto.request.ChatbotRequest;
import com.goodsple.features.chatbot.dto.response.ChatbotResponse;
import com.goodsple.features.chatbot.mapper.ChatbotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final ChatbotMapper chatbotMapper;

    @Value("${chatbot-api.url}")
    private String pythonBase;

    @Transactional
    public ChatbotResponse handle(ChatbotRequest req) {

        ChatbotResponse res;

        /* 1️⃣ 답변 생성 */
        if ("FAQ".equalsIgnoreCase(req.getSource())) {

            String answer = chatbotMapper.getAnswerByQuestion(req.getText());

            res = ChatbotResponse.builder()
                    .answer(answer != null ? answer : "등록된 답변이 없습니다.")
                    .intent("FAQ")
                    .confidence(1.0)
                    .build();
        }
        else {
            try {
                String url = pythonBase + "/answer";
                res = restTemplate.postForObject(
                        url,
                        Map.of("text", req.getText()),
                        ChatbotResponse.class
                );
                if (res == null) res = fallback();
            } catch (Exception e) {
                res = fallback();
            }
        }

        /* 2️⃣ 로그 ID 결정 */
        Long logId;

        if (Boolean.TRUE.equals(req.getIsNewChat())) {

            // 🔥 무조건 새 로그 생성
            ChatbotLog log = ChatbotLog.builder()
                    .logSessionId(req.getSessionId())
                    .userId(req.getUserId())
                    .logType(req.getSource())
                    .logInitialQuestion(req.getText())
                    .logPredictedIntent(res.getIntent())
                    .logConfidenceScore(res.getConfidence())
                    .build();

            chatbotMapper.insertChatLog(log);
            logId = log.getLogId();

        } else {
            logId = req.getLogId();
        }

        if (logId == null) {
            throw new IllegalStateException("chatbot logId 생성 실패");
        }

        /* 3️⃣ 메시지 저장 */
        int nextOrder = chatbotMapper.findNextMessageOrder(logId);
        saveMessage(logId, "user", req.getText(), nextOrder);
        saveMessage(logId, "bot", res.getAnswer(), nextOrder + 1);

        res.setLogId(logId);
        return res;
    }



    private void saveMessage(Long logId, String sender, String text, int order) {
        ChatbotMessage msg = ChatbotMessage.builder()
                .logId(logId)
                .messageSender(sender)
                .messageText(text)
                .messageOrderIndex(order)
                .build();
        chatbotMapper.insertChatMessage(msg);
    }


    private ChatbotResponse fallback() {
        return ChatbotResponse.builder()
//                .logId(null)
                .answer("일시적인 오류입니다. 잠시 후 다시 시도해주세요.")
                .intent("fallback")
                .confidence(0.0)
                .build();
    }


    public List<String> getFaqIntents() {
        return chatbotMapper.findFaqIntents();
    }

    public List<String> getQuestionsByIntent(String intent) {
        return chatbotMapper.getQuestionsByIntent(intent);
    }

    public String getAnswerByQuestion(String question) {
        return chatbotMapper.getAnswerByQuestion(question);
    }

}
