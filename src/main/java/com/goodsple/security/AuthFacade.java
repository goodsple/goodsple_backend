package com.goodsple.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade implements CurrentUser {

    @Override
    public Long userId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new InsufficientAuthenticationException("Not authenticated");
        }

        Object p = auth.getPrincipal();

        // 1) 우리 커스텀 프린시펄만으로 해결 (getUser() 필요 없음)
        if (p instanceof CustomUserDetails cud) {
            return cud.getUserId();
        }

        // 2) 스프링 기본 UserDetails(username에 id가 들어있는 경우만)
        if (p instanceof org.springframework.security.core.userdetails.User ud) {
            try { return Long.valueOf(ud.getUsername()); } catch (NumberFormatException ignored) {}
        }

        // 3) Map 형태의 클레임을 principal로 쓰는 구현 방어
        if (p instanceof java.util.Map<?, ?> claims) {
            Object v = claims.get("userId");
            if (v != null) return Long.valueOf(v.toString());
            Object sub = claims.get("sub");
            if (sub != null) try { return Long.valueOf(sub.toString()); } catch (NumberFormatException ignored) {}
        }

        // 4) String principal (예: "anonymousUser" 또는 "25")
        if (p instanceof String s) {
            if ("anonymousUser".equalsIgnoreCase(s)) {
                throw new InsufficientAuthenticationException("Anonymous user");
            }
            try { return Long.valueOf(s); } catch (NumberFormatException ignored) {}
            try { return Long.valueOf(auth.getName()); } catch (NumberFormatException ignored) {}
        }

        throw new IllegalStateException("Unsupported principal type: " + p.getClass());
    }
}
