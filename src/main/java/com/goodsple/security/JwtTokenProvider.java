package com.goodsple.security;

import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    /**
     * JWT 토큰 생성 및 검증, Authentication 변환을 담당하는 컴포넌트
     */

    @Value("${jwt.secret}")
    private String secretKey;                  // application.yml에서 주입받은 Base64 인코딩된 비밀키

    @Value("${jwt.access-validity}")
    private long accessTokenValidityMs;        // 액세스 토큰(짧은 기간) 유효시간 (밀리초 단위)

    @Value("${jwt.refresh-validity}")
    private long refreshTokenValidityMs;       // 리프레시 토큰(긴 기간) 유효시간 (밀리초 단위)

    private Key key;                           // 서명(Signature)용 Key 객체

    private final UserMapper userMapper;

    /**
     * 빈 초기화 단계에서 호출
     * Base64로 인코딩된 secretKey를 디코딩해서 HMAC-SHA 키 객체 생성
     */
    @PostConstruct
    public void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 액세스 토큰 생성
     * @param userId 유저 고유 ID를 subject로 설정
     * @param role   유저 권한을 "role" 클레임으로 첨부
     * @return 생성된 JWT 문자열
     */
    public String createAccessToken(Long userId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId.toString())                                  // JWT의 주제(subject)
                .claim("role", role)                                            // 추가 클레임에 권한 정보
                .setIssuedAt(now)                                               // 발급 시간 (iat)
                .setExpiration(new Date(now.getTime() + accessTokenValidityMs)) // 만료 시간 (exp)
                .signWith(key, SignatureAlgorithm.HS256)                        // 서명 알고리즘 및 키
                .compact();                                                     // JWT 문자열로 직렬화
    }

    /**
     * 리프레시 토큰 생성
     * @param userId 유저 고유 ID를 subject로
     * @param role   유저 권한을 "role" 클레임으로 첨부
     * @return 생성된 JWT 문자열
     */
    public String createRefreshToken(Long userId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidityMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검사
     * @param token 클라이언트가 보낸 JWT
     * @return 토큰이 정상적(만료·위변조 X) 이면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)    // 서명 검증 키 설정
                    .build()
                    .parseClaimsJws(token); // 파싱 및 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 만료, 변조 등 예외 발생 시 false 리턴
            return false;
        }
    }


    /**
     * 토큰으로부터 Authentication 객체 생성
     * principal에 CustomUserDetails 세팅하여 컨트롤러에서 바로 사용 가능
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long userId = Long.valueOf(claims.getSubject());

        // 1) 유저 조회
        User u = userMapper.findById(userId);
        if (u == null) {
            // 토큰상 userId가 DB에 없으면 인증 실패 처리
            throw new UsernameNotFoundException("No user found with id: " + userId);
            // 또는 return null; 하고, 필터에서 null 체크 후 SecurityContext에 세팅하지 않도록 해도 된다.
        }

        // 상태 체크: 탈퇴/정지면 인증 객체를 만들지 않음
        String status = String.valueOf(u.getSuspensionStatus()).toLowerCase();
        if ("withdrawn".equals(status) || "suspended".equals(status)) {
            // 필터에서 auth == null이면 SecurityContext에 세팅하지 않으므로 자연스럽게 거부됨
            return null;
            // (원하면) throw new UsernameNotFoundException("Inactive user"); 로도 가능
        }

        String role = claims.get("role", String.class);

        // 권한 리스트 생성 (ROLE_ 접두사 포함)
        List<GrantedAuthority> auths = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        // CustomUserDetails에 User 엔티티와 권한 목록 래핑
        CustomUserDetails principal = new CustomUserDetails(u, auths);

        // Authentication 토큰 생성 (credentials 자리에 raw token 보관 가능)
        return new UsernamePasswordAuthenticationToken(
                principal,
                token,
                principal.getAuthorities()
        );
    }

    /** userId 기준으로 active 여부 판단 */
    public boolean isUserActive(Long userId) {
        User u = userMapper.findById(userId);
        if (u == null) return false;
        String st = String.valueOf(u.getSuspensionStatus()).toLowerCase(); // active/suspended/withdrawn
        return !"withdrawn".equals(st) && !"suspended".equals(st);
    }

    // subject에서 userId 읽기 (키 재사용)
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token); // 키(key) 재사용
        return Long.valueOf(claims.getSubject()); // subject = userId
    }

    /**
     * 토큰에서 subject(userId)를 꺼내는 유틸
     */
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    /**
     * 토큰에서 role 클레임을 꺼내는 유틸
     */
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    /** 중복된 Claims 파싱 로직을 메서드로 분리 */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

