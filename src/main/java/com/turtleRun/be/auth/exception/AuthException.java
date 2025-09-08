package com.turtleRun.be.auth.exception;

import com.turtleRun.be.common.exception.ErrorCode;
import com.turtleRun.be.common.exception.TurtleRunException;

import java.util.Map;

/**
 * 인증 관련 예외
 * 인증 도메인의 비즈니스 예외를 처리합니다.
 */
public class AuthException extends TurtleRunException {
    
    public AuthException(ErrorCode errorCode, String message, Map<String, Object> parameters) {
        super(errorCode, message, parameters);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    // 정적 중첩 클래스들
    public static class UserNotFound extends AuthException {
        public UserNotFound(String identifier) {
            super(ErrorCode.AUTH_USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + identifier, Map.of("identifier", identifier));
        }
    }
    
    public static class InvalidCredentials extends AuthException {
        public InvalidCredentials() {
            super(ErrorCode.AUTH_INVALID_CREDENTIALS, "잘못된 인증 정보입니다");
        }
    }
    
    public static class EmailAlreadyExists extends AuthException {
        public EmailAlreadyExists(String email) {
            super(ErrorCode.AUTH_EMAIL_ALREADY_EXISTS, "이미 존재하는 이메일입니다: " + email, Map.of("email", email));
        }
    }
    
    public static class UsernameAlreadyExists extends AuthException {
        public UsernameAlreadyExists(String username) {
            super(ErrorCode.AUTH_USERNAME_ALREADY_EXISTS, "이미 존재하는 사용자명입니다: " + username, Map.of("username", username));
        }
    }
    
    public static class InvalidToken extends AuthException {
        public InvalidToken() {
            super(ErrorCode.AUTH_INVALID_TOKEN, "유효하지 않은 토큰입니다");
        }
        
        public InvalidToken(String message) {
            super(ErrorCode.AUTH_INVALID_TOKEN, "유효하지 않은 토큰입니다: " + message);
        }
    }
    
    public static class TokenExpired extends AuthException {
        public TokenExpired() {
            super(ErrorCode.AUTH_TOKEN_EXPIRED, "토큰이 만료되었습니다");
        }
    }
    
    public static class UserInactive extends AuthException {
        public UserInactive() {
            super(ErrorCode.AUTH_USER_INACTIVE, "비활성화된 사용자입니다");
        }
    }
    
    public static class EmailNotVerified extends AuthException {
        public EmailNotVerified() {
            super(ErrorCode.AUTH_EMAIL_NOT_VERIFIED, "이메일이 인증되지 않았습니다");
        }
    }
    
    public static class InvalidInput extends AuthException {
        public InvalidInput(String message) {
            super(ErrorCode.AUTH_INVALID_INPUT, "잘못된 입력입니다: " + message, Map.of("message", message));
        }
    }
    
    public static class InternalServerError extends AuthException {
        public InternalServerError(String message) {
            super(ErrorCode.AUTH_INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다: " + message, Map.of("message", message));
        }
    }
}
