package com.turtleRun.be.auth.application.service;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰 서비스
 * JWT 토큰의 생성, 검증, 파싱을 담당합니다.
 */
@Service
public class JwtTokenService {
    
    private final JwtProperties jwtProperties;
    
    public JwtTokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    
    /**
     * 사용자 정보로부터 JWT 토큰을 생성합니다.
     * 
     * @param user 사용자 정보
     * @return JWT 토큰
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().getValue().toString());
        claims.put("username", user.getUsername().getValue());
        claims.put("email", user.getEmail().getValue());
        claims.put("name", user.getName().getValue());
        
        return createToken(claims, user.getUsername().getValue());
    }
    
    /**
     * JWT 토큰을 생성합니다.
     * 
     * @param claims 클레임 정보
     * @param subject 주체 (사용자명)
     * @return JWT 토큰
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * JWT 토큰에서 사용자명을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 사용자명
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * JWT 토큰에서 사용자 ID를 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }
    
    /**
     * JWT 토큰에서 만료 시간을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * JWT 토큰에서 특정 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @param claimsResolver 클레임 추출 함수
     * @return 클레임 값
     */
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * JWT 토큰에서 모든 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 모든 클레임
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * JWT 토큰이 만료되었는지 확인합니다.
     * 
     * @param token JWT 토큰
     * @return 만료 여부
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * JWT 토큰의 기본 유효성을 검증합니다 (서명, 만료시간 등).
     * 
     * @param token JWT 토큰
     * @return 유효성 여부
     */
    public Boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * JWT 토큰의 유효성을 검증합니다.
     * 
     * @param token JWT 토큰
     * @param username 사용자명
     * @return 유효성 여부
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    
    /**
     * 토큰 만료 시간을 반환합니다.
     * 
     * @return 만료 시간 (밀리초)
     */
    public long getExpirationTime() {
        return jwtProperties.getExpiration();
    }
}
