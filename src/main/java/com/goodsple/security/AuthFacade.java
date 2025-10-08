package com.goodsple.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 현재 로그인 사용자 userId 를 돌려주는 어댑터.
 * ChatController.Auth, ChatWsController.Auth 둘 다 구현해서 자동 주입되게 한다.
 */
@Component
public class AuthFacade implements CurrentUser {

    @Override
    public Long userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Unauthenticated");
        }

        Object principal = authentication.getPrincipal();

        // ① 커스텀 프린시펄 사용 시 (추천: CustomUserDetails에 userId 필드가 있어야 함)
        if (principal instanceof CustomUserDetails u) {
            return u.getUserId();
        }

        // ② JWT 필터가 claims(Map)를 principal로 넣는 경우
        if (principal instanceof java.util.Map<?, ?> claims) {
            Object v = claims.get("userId");
            if (v != null) return Long.valueOf(v.toString());
        }

        // ③ username(이메일)만 있는 경우 -> 필요시 UserService로 변환하도록 확장
        if (principal instanceof String username) {
            // TODO: userService.findIdByEmail(username)
        }

        throw new IllegalStateException("Unsupported principal type: " + principal.getClass());
    }
}
