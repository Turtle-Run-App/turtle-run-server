package com.turtleRun.be.auth.application.service;

import com.turtleRun.be.auth.application.dto.SignUpRequestDto;
import com.turtleRun.be.auth.application.dto.LoginRequestDto;
import com.turtleRun.be.auth.application.dto.SignUpResponseDto;
import com.turtleRun.be.auth.application.dto.LoginResponseDto;

/**
 * 인증 애플리케이션 서비스 인터페이스
 * DDD에서 Application Service는 도메인 서비스들을 조합하여 애플리케이션의 유스케이스를 처리합니다.
 */
public interface AuthApplicationService {
    
    /**
     * 사용자 회원가입을 처리합니다.
     * 
     * @param request 회원가입 요청 정보
     * @return 회원가입 결과
     */
    SignUpResponseDto signUp(SignUpRequestDto request);
    
    /**
     * 사용자 로그인을 처리합니다.
     * 
     * @param request 로그인 요청 정보
     * @return 로그인 결과 (JWT 토큰 포함)
     */
    LoginResponseDto login(LoginRequestDto request);
    
    /**
     * 사용자 정보를 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    SignUpResponseDto getUserInfo(String userId);
}
