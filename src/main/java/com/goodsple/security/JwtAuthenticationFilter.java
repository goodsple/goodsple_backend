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
        // 1) ContextPath 를 제외한 순수 URI 가져오기
        String servletPath = request.getServletPath(); // e.g. "/api/auth/kakao/callback"

        // 2) 스킵 여부 판단
        boolean skip = servletPath.startsWith("/api/auth/");

        // 3) (선택) 스킵할 때 로그 찍기
        if (skip) {
            System.out.println("[JwtFilter] SKIP 인증 필터 for path: " + servletPath);
        }

        // 4) 한 번만 리턴!
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String path = request.getRequestURI();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 여긴 JWT 검증 로직만!
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            if (jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 4) 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
