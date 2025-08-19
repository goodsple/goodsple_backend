package com.goodsple.features.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SubscriptionTrackerInterceptor subscriptionTrackerInterceptor;
    private final ChannelInterceptor websocketAuthInterceptor;

//    // @Lazy로 순환 참조 방지
//    public WebSocketConfig(@Lazy SubscriptionTrackerInterceptor subscriptionTrackerInterceptor) {
//        this.subscriptionTrackerInterceptor = subscriptionTrackerInterceptor;
//    }

    public WebSocketConfig(
            @Lazy SubscriptionTrackerInterceptor subscriptionTrackerInterceptor,
            @Lazy ChannelInterceptor websocketAuthInterceptor // SecurityConfig 에서 @Bean 등록된 그 인터셉터
    ) {
               this.subscriptionTrackerInterceptor = subscriptionTrackerInterceptor;
               this.websocketAuthInterceptor = websocketAuthInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 인터셉터를 inbound 채널에 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(subscriptionTrackerInterceptor);
        registration.interceptors(
                websocketAuthInterceptor,       // 먼저 JWT 인증 부여
                subscriptionTrackerInterceptor  // 그 다음 구독/인원수 추적
        );
    }
}
