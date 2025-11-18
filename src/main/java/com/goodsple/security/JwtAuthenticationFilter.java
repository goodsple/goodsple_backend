package com.goodsple.security;

import jakarta.servlet.DispatcherType;
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
        // 에러 디스패치/에러 경로는 필터 스킵
        if (request.getDispatcherType() == DispatcherType.ERROR) return true;
        if ("/error".equals(request.getServletPath())) return true;

        String path = request.getServletPath();
        // 로그인/회원가입/토큰 재발급/Kakao 콜백 등 인증이 _아직_ 불필요한 엔드포인트만 열어둡니다.
        if (path.equals("/api/auth/login")
                || path.equals("/api/auth/signup")
                || path.equals("/api/auth/refresh")
                || path.startsWith("/api/auth/kakao/")) {
            return true;
        }
        // Swagger, API Docs
        if (path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/")) {
            return true;
        }
        // 그 외 모든 /api/auth/** 요청에 대해서는 필터를 적용하여
        // Authorization 헤더의 JWT를 꺼내고, SecurityContext에 인증 정보를 채웁니다.
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // (선택) 프리플라이트 스킵
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserIdFromToken(token);

                // 탈퇴/정지 계정 차단
                if (jwtProvider.isUserActive(userId)) {
                    Authentication auth = jwtProvider.getAuthentication(token); // principal = CustomUserDetails
                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                // 토큰 없거나 유효하지 않으면 인증 세팅하지 않음(익명 처리)
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            // 어떤 예외도 보안컨텍스트를 오염시키지 않게 클리어
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 1) resolveToken() 로 헤더→쿠키 순서대로 토큰 꺼내기
//        String token = resolveToken(request);
//        String path = request.getServletPath();
//
//        System.out.println("[JWT] path=" + path + ", hasToken=" + (token != null));
//
//        boolean valid = token != null && jwtProvider.validateToken(token);
//        System.out.println("[JWT] valid=" + valid);
//
//        if (valid) {
//            try {
//                Authentication auth = jwtProvider.getAuthentication(token);
//                System.out.println("[JWT] auth=" + (auth != null) + ", roles=" + (auth != null ? auth.getAuthorities() : "null"));
//                if (auth != null) {
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                }
//            } catch (UsernameNotFoundException ex) {
//                System.out.println("[JWT] user not found, clear context");
//                SecurityContextHolder.clearContext();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                SecurityContextHolder.clearContext();
//            }
//        }
//
//        // 2) 유효성 검사 및 SecurityContext 설정
//        if (token != null && jwtProvider.validateToken(token)) {
//            try {
//                // 토큰 속 userId 뽑아서 상태 확인
//                Long userId = jwtProvider.getUserIdFromToken(token);
//                boolean active = jwtProvider.isUserActive(userId); // 내부에서 userMapper.findById(...)로 체크
//                if (!active) {
//                    // 탈퇴/정지면 인증하지 않음
//                    SecurityContextHolder.clearContext();
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//                Authentication auth = jwtProvider.getAuthentication(token);
//                if (auth != null) {
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                }
//            } catch (UsernameNotFoundException ex) {
//                // 토큰은 통과했지만 유저가 DB에 없으면 인증 무효 처리
//                SecurityContextHolder.clearContext();
//            }
//        }
//
//        // 3) 필터 체인 계속 진행
//        filterChain.doFilter(request, response);
//    }

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
