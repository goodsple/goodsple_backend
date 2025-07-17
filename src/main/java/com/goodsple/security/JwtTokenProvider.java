package com.goodsple.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${jwt.secret}")
    private String secretKey;                  // application.yml에서 주입받은 Base64 인코딩된 비밀키

    @Value("${jwt.access-validity}")
    private long accessTokenValidityMs;        // 액세스 토큰(짧은 기간) 유효시간 (밀리초 단위)

    @Value("${jwt.refresh-validity}")
    private long refreshTokenValidityMs;       // 리프레시 토큰(긴 기간) 유효시간 (밀리초 단위)

    private Key key;                           // 서명(Signature)용 Key 객체

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
     * 토큰으로부터 스프링 시큐리티 Authentication 객체 생성
     * @param token 검증된 JWT
     * @return 인증 정보(Authentication) – SecurityContext에 저장
     */
    public Authentication getAuthentication(String token) {
        // 1) Claims(내용) 파싱
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 2) subject에서 userId 꺼내기
        Long userId = Long.valueOf(claims.getSubject());
        // 3) "role" 클레임에서 권한 정보 꺼내기
        String role = claims.get("role", String.class);

        // 4) Spring Security용 UserDetails 객체 생성
        //    - username엔 userId를, password는 이미 인증됐으니 빈 문자열
        UserDetails principal = User.withUsername(userId.toString())
                .password("")
                .roles(role)
                .build();

        // 5) UsernamePasswordAuthenticationToken으로 래핑해 반환
        return new UsernamePasswordAuthenticationToken(
                principal,
                token,
                principal.getAuthorities()
        );
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

