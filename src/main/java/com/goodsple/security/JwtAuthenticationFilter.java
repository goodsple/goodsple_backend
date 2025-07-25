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

    /**
     * true 를 리턴하는 요청은 doFilterInternal 자체를 스킵합니다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println("▶ servletPath: " + path + ", requestURI: " + request.getRequestURI());

        // 로그인 없이 열어둘 API만 true 리턴
        boolean skip =
                path.equals("/api/auth/find-id/request")
                        || path.equals("/api/auth/find-id")
                        || path.equals("/api/auth/login")
                        || path.equals("/api/auth/signup")
                        || path.startsWith("/swagger-ui/")
                        || path.startsWith("/v3/api-docs/");

        if (skip) {
            System.out.println("[JwtFilter] SKIP 인증 필터 for path: " + path);
        }
        return skip;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) 토큰 파싱
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            // 2) 토큰 유효성 검사
            if (jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 3) 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
