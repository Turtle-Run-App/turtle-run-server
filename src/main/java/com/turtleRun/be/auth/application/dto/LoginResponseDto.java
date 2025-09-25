package com.turtleRun.be.auth.application.dto;

/**
 * 로그인 응답 DTO
 * 로그인 성공 시 클라이언트에게 반환하는 정보를 담습니다.
 */
public class LoginResponseDto {
    
    private boolean success;
    private String message;
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UserInfoDto user;

    // 기본 생성자
    public LoginResponseDto() {}

    // 생성자
    public LoginResponseDto(boolean success, String message, String accessToken, 
                           String tokenType, long expiresIn, UserInfoDto user) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // 정적 팩토리 메서드
    public static LoginResponseDto success(String accessToken, long expiresIn, UserInfoDto user) {
        return new LoginResponseDto(true, "로그인이 성공적으로 완료되었습니다", 
                                  accessToken, "Bearer", expiresIn, user);
    }

    public static LoginResponseDto failure(String message) {
        return new LoginResponseDto(false, message, null, null, 0, null);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", accessToken='[PROTECTED]'" +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", user=" + user +
                '}';
    }
}
