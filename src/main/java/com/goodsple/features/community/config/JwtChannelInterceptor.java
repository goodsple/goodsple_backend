package com.goodsple.features.community.config;

import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        // CONNECT 요청 시 JWT 인증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalStateException("JWT 토큰이 없습니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            if (auth == null) {
                throw new IllegalStateException("JWT 인증 실패");
            }

            accessor.setUser(auth); // 이후 controller에서 @AuthenticationPrincipal 가능
        }

        return message;
    }
}
