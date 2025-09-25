package com.turtleRun.be.auth.application.dto;

/**
 * 회원가입 응답 DTO
 * 회원가입 성공 시 클라이언트에게 반환하는 정보를 담습니다.
 */
public class SignUpResponseDto {
    
    private boolean success;
    private String message;
    private UserInfoDto user;

    // 기본 생성자
    public SignUpResponseDto() {}

    // 생성자
    public SignUpResponseDto(boolean success, String message, UserInfoDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // 정적 팩토리 메서드
    public static SignUpResponseDto success(UserInfoDto user) {
        return new SignUpResponseDto(true, "회원가입이 성공적으로 완료되었습니다", user);
    }

    public static SignUpResponseDto failure(String message) {
        return new SignUpResponseDto(false, message, null);
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

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SignUpResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
