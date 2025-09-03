package com.turtleRun.be.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 DTO
 * 클라이언트로부터 받는 로그인 정보를 담습니다.
 */
public class LoginRequestDto {
    
    @NotBlank(message = "사용자명 또는 이메일은 필수입니다")
    private String usernameOrEmail;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    // 기본 생성자
    public LoginRequestDto() {}

    // 생성자
    public LoginRequestDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}
