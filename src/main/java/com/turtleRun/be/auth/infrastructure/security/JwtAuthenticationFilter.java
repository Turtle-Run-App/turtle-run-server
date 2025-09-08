package com.turtleRun.be.auth.infrastructure.security;

import com.turtleRun.be.auth.application.service.JwtTokenService;
import com.turtleRun.be.auth.domain.valueobject.Username;
import com.turtleRun.be.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * HTTP 요청에서 JWT 토큰을 추출하고 인증을 처리합니다.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    
    public JwtAuthenticationFilter(JwtTokenService jwtTokenService,
                                 UserDetailsService userDetailsService,
                                 UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);
            
            if (jwt != null) {
                // JWT 토큰 기본 검증 (서명, 만료시간 등)
                if (jwtTokenService.validateToken(jwt)) {
                    String username = jwtTokenService.getUsernameFromToken(jwt);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        // 사용자 정보와 함께 토큰 재검증
                        if (jwtTokenService.validateToken(jwt, userDetails.getUsername())) {
                            UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.debug("JWT 인증 성공: {}", username);
                        } else {
                            logger.warn("JWT 토큰 검증 실패: {}", username);
                        }
                    }
                } else {
                    logger.warn("JWT 토큰이 유효하지 않습니다");
                }
            }
        } catch (Exception e) {
            logger.error("JWT 인증 처리 중 오류 발생", e);
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * HTTP 요청에서 JWT 토큰을 추출합니다.
     * 
     * @param request HTTP 요청
     * @return JWT 토큰 또는 null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}
