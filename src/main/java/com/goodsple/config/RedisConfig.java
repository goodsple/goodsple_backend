/**
 * 파일 경로: src/main/java/com/goodsple/config/RedisConfig.java
 * 설명: Spring Data Redis를 설정하여 Redis 서버와의 통신을 구성합니다.
 */
package com.goodsple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key는 String으로 직렬화합니다. (예: "auction:123")
        template.setKeySerializer(new StringRedisSerializer());

        // Value는 JSON 형태로 직렬화합니다. 
        // 이렇게 하면 Java 객체를 그대로 Redis에 저장하고, 다시 객체로 쉽게 변환할 수 있습니다.
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash의 Key와 Value도 동일하게 설정합니다.
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}