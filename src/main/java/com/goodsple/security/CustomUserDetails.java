package com.goodsple.security;

import com.goodsple.features.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * CustomUserDetails: 스프링 시큐리티 인증 컨텍스트에 저장될 사용자 정보 구현체
 * - User 엔티티를 래핑하여 도메인 식별자인 userId를 포함
 * - UserDetails 인터페이스를 구현하여 username, password, authorities를 제공합니다
 */
public class CustomUserDetails implements UserDetails {
    // 서비스 도메인 식별자
    private final Long userId;
    // 로그인 ID
    private final String username;
    // 암호화된 비밀번호
    private final String password;
    // 사용자 권한 목록
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 생성자: User 엔티티와 권한 목록을 받아 CustomUserDetails로 변환
     */
    public CustomUserDetails(User u, Collection<? extends GrantedAuthority> auths) {
        this.userId      = u.getUserId();
        this.username    = u.getLoginId();
        this.password    = u.getPassword();
        this.authorities = auths;
    }

    /**
     * 도메인 식별자 반환 (컨트롤러 등에서 사용)
     */
    public Long getUserId() {
        return userId;
    }

    // UserDetails 필수 구현 메서드 =======================================
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}