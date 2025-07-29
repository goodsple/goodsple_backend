package com.goodsple.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 검증하여 스프링 시큐리티 컨텍스트에 인증 정보를 설정하는 필터
 */
@RequiredArgsConstructor
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

        System.out.println("▶ JWT Filter on path: " + request.getServletPath());
        // 1) resolveToken() 로 헤더→쿠키 순서대로 토큰 꺼내기
        String token = resolveToken(request);

        // 2) 유효성 검사 및 SecurityContext 설정
        if (token != null && jwtProvider.validateToken(token)) {
            try {
                Authentication auth = jwtProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (UsernameNotFoundException ex) {
                // 토큰은 통과했지만 유저가 DB에 없으면 인증 무효 처리
                SecurityContextHolder.clearContext();
            }
        }

        // 3) 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1) Authorization 헤더 우선
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        // 2) 다음으로 쿠키
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
