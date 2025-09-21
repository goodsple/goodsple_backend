package com.goodsple.features.community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 방별 온라인 인원 카운트 관리
 * 프론트 구독 경로: /topic/{roomId}, /topic/roomUsers/{roomId}
 */
@Component
@RequiredArgsConstructor
public class SubscriptionTrackerInterceptor implements ChannelInterceptor {

    private final SimpMessagingTemplate template;

    // roomId -> sessionId Set
    private final Map<String, Set<String>> roomSessions = new ConcurrentHashMap<>();
    // sessionId -> roomId Set
    private final Map<String, Set<String>> sessionRooms = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) return message;

        String sessionId = accessor.getSessionId();

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String roomId = extractRoomId(accessor.getDestination());
            if (roomId != null) {
                roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
                sessionRooms.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(roomId);
                broadcastOnlineCount(roomId);
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            Set<String> rooms = sessionRooms.remove(sessionId);
            if (rooms != null) {
                for (String roomId : rooms) {
                    Set<String> sessions = roomSessions.get(roomId);
                    if (sessions != null) {
                        sessions.remove(sessionId);
                        if (sessions.isEmpty()) roomSessions.remove(roomId);
                        broadcastOnlineCount(roomId);
                    }
                }
            }
        }

        return message;
    }

    private void broadcastOnlineCount(String roomId) {
        int count = roomSessions.getOrDefault(roomId, Set.of()).size();
        template.convertAndSend("/topic/roomUsers/" + roomId, count);
    }

    private String extractRoomId(String destination) {
        if (destination == null || !destination.startsWith("/topic/")) return null;
        String[] parts = destination.split("/");
        // /topic/{roomId} 또는 /topic/roomUsers/{roomId}
        if (parts.length >= 3) {
            if ("roomUsers".equals(parts[2]) && parts.length >= 4) return parts[3];
            return parts[2];
        }
        return null;
    }
}
