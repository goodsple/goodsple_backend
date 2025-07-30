package com.goodsple.config;

import com.goodsple.security.JwtAuthenticationFilter;
import com.goodsple.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.util.List;

@Configuration
public class SecurityConfig {
    // 보안(인증/인가) 설정 (Spring Security)

    private final JwtTokenProvider jwtProvider;

    public SecurityConfig(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보안 기능 끄기 (API 서버라서)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173")); // 프론트 도메인 허용
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    // 여기에 Authorization, Content-Type 등 구체적으로 명시
//                    config.setAllowedHeaders(List.of("Authorization","Content-Type","Accept"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용 → 세션 안 씀
                )
                .formLogin(form -> form.disable()) // 폼 로그인 사용 안 함
                .httpBasic(basic -> basic.disable()) // 기본 인증창 안 띄움
                // 인증/권한 에러 발생 시 JSON 응답 주기 위한 설정
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint()) // 로그인 안 했을 때 401 처리
                        .accessDeniedHandler(accessDeniedHandler())           // 권한 없을 때 403 처리
                )
                .authorizeHttpRequests(auth -> auth
                        // 1) CORS preflight(OPTIONS)를 인증 없이 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()

                        // 2) 로그인 전 허용 경로
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reports/reasons").permitAll()

                        // 신고 등록은 로그인 필요
                        .requestMatchers(HttpMethod.POST, "/api/reports").authenticated()

                        // 3) 그 외 모든 요청은 JWT 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider),
                        SecurityContextPersistenceFilter.class
                );

        return http.build();
    }

    // 로그인 안 했을 때 에러 처리 (401)
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"❗아이디 또는 비밀번호가 일치하지 않습니다.\"}");
        };
    }

    // 권한 없을 때 에러 처리 (403)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"❗접근 권한이 없습니다.\"}");
        };
    }

    // 비밀번호 암호화 (회원가입, 로그인에 사용)
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 암호화 (Spring Security에서 제공)
        // Configuration에서 Bean으로 등록해서 사용하는 방식
        return new BCryptPasswordEncoder();
    }

}
