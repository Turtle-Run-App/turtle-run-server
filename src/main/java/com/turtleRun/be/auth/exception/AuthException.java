package com.turtleRun.be.auth.exception;

import com.turtleRun.be.common.exception.TurtleRunException;

/**
 * 인증 관련 예외
 * 인증 도메인의 비즈니스 예외를 처리합니다.
 */
public class AuthException extends TurtleRunException {
    
    public AuthException(String message) {
        super(message);
    }
    
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // 정적 중첩 클래스들
    public static class UserNotFound extends AuthException {
        public UserNotFound(String identifier) {
            super("사용자를 찾을 수 없습니다: " + identifier);
        }
    }
    
    public static class InvalidCredentials extends AuthException {
        public InvalidCredentials() {
            super("잘못된 인증 정보입니다");
        }
    }
    
    public static class EmailAlreadyExists extends AuthException {
        public EmailAlreadyExists(String email) {
            super("이미 존재하는 이메일입니다: " + email);
        }
    }
    
    public static class UsernameAlreadyExists extends AuthException {
        public UsernameAlreadyExists(String username) {
            super("이미 존재하는 사용자명입니다: " + username);
        }
    }
    
    public static class InvalidToken extends AuthException {
        public InvalidToken() {
            super("유효하지 않은 토큰입니다");
        }
        
        public InvalidToken(String message) {
            super("유효하지 않은 토큰입니다: " + message);
        }
    }
    
    public static class TokenExpired extends AuthException {
        public TokenExpired() {
            super("토큰이 만료되었습니다");
        }
    }
    
    public static class UserInactive extends AuthException {
        public UserInactive() {
            super("비활성화된 사용자입니다");
        }
    }
    
    public static class EmailNotVerified extends AuthException {
        public EmailNotVerified() {
            super("이메일이 인증되지 않았습니다");
        }
    }
}
