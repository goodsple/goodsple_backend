package com.goodsple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 6.1 이상 방식
                .formLogin(form ->form.disable()) // 폼 로그인 비활성화
                .httpBasic(httpBasic ->httpBasic.disable()) // HTTP Basic 비활성화
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/auth/**",         // 회원가입, 로그인 등
//                                "/swagger-ui/**",       // Swagger UI
//                                "/v3/api-docs/**",      // Swagger JSON Docs
//                                "/swagger-resources/**",// Swagger 리소스
//                                "/webjars/**"           // Swagger 웹자원
//                        ).permitAll()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 암호화 (Spring Security에서 제공)
        // Configuration에서 Bean으로 등록해서 사용하는 방식
        return new BCryptPasswordEncoder();
    }

}
