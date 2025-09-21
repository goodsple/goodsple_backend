/**
 * 파일 경로: src/main/java/com/goodsple/config/SwaggerConfig.java
 * 설명: Swagger UI에 JWT 인증을 위한 "Authorize" 버튼을 추가하는 설정 파일입니다.
 */
package com.goodsple.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. JWT라는 이름의 보안 스킴 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // HTTP 방식
                .scheme("bearer")               // 토큰 타입은 bearer
                .bearerFormat("JWT")            // 포맷은 JWT
                .in(SecurityScheme.In.HEADER)   // 헤더에 위치
                .name("Authorization");         // 헤더 이름은 Authorization

        // 2. 보안 요구사항 정의: 위에서 정의한 JWT 스킴을 사용하도록 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");

        // 3. OpenAPI 객체에 컴포넌트(보안 스킴)와 보안 요구사항 추가
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT", securityScheme))
                .addSecurityItem(securityRequirement); // 수정된 부분: addSecurityRequirement -> addSecurityItem
    }
}