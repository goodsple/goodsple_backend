package com.goodsple.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 검증하여 스프링 시큐리티 컨텍스트에 인증 정보를 설정하는 필터
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 1) HTTP 헤더에서 Authorization 토큰을 가져옵니다.
        String bearer = request.getHeader("Authorization");

        // 2) "Bearer " 접두사가 있는지 확인하고, 유효한 토큰인지 검사합니다.
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            if (jwtProvider.validateToken(token)) {
                // 3) 토큰이 유효하면 Authentication 객체를 생성하여 시큐리티 컨텍스트에 설정
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 4) 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
