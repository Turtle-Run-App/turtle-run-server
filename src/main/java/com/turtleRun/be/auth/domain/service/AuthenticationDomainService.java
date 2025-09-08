package com.turtleRun.be.auth.domain.service;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.Password;
import com.turtleRun.be.auth.domain.valueobject.Username;

/**
 * 인증 도메인 서비스
 * DDD에서 Domain Service는 여러 엔티티에 걸친 비즈니스 로직을 처리합니다.
 */
public interface AuthenticationDomainService {
    
    /**
     * 사용자 인증을 수행합니다.
     * 
     * @param user 인증할 사용자
     * @param plainPassword 평문 비밀번호
     * @return 인증 성공 여부
     */
    boolean authenticate(User user, String plainPassword);
    
    /**
     * 이메일 중복 여부를 확인합니다.
     * 
     * @param email 확인할 이메일
     * @return 중복 여부 (true: 중복됨, false: 사용 가능)
     */
    boolean isEmailDuplicate(Email email);
    
    /**
     * 사용자명 중복 여부를 확인합니다.
     * 
     * @param username 확인할 사용자명
     * @return 중복 여부 (true: 중복됨, false: 사용 가능)
     */
    boolean isUsernameDuplicate(Username username);
    
    /**
     * 새 사용자를 등록합니다.
     * 
     * @param user 등록할 사용자
     * @return 등록된 사용자
     */
    User register(User user);
}
