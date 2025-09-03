package com.turtleRun.be.auth.repository;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.UserId;
import com.turtleRun.be.auth.domain.valueobject.Username;

import java.util.Optional;

/**
 * 사용자 저장소 인터페이스
 * DDD에서 Repository는 도메인 객체의 영속성을 관리합니다.
 */
public interface UserRepository {
    
    /**
     * 사용자를 저장합니다.
     * 
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    User save(User user);
    
    /**
     * ID로 사용자를 조회합니다.
     * 
     * @param id 사용자 ID
     * @return 사용자 (Optional)
     */
    Optional<User> findById(UserId id);
    
    /**
     * 이메일로 사용자를 조회합니다.
     * 
     * @param email 이메일
     * @return 사용자 (Optional)
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * 사용자명으로 사용자를 조회합니다.
     * 
     * @param username 사용자명
     * @return 사용자 (Optional)
     */
    Optional<User> findByUsername(Username username);
    
    /**
     * 이메일 중복 여부를 확인합니다.
     * 
     * @param email 확인할 이메일
     * @return 중복 여부
     */
    boolean existsByEmail(Email email);
    
    /**
     * 사용자명 중복 여부를 확인합니다.
     * 
     * @param username 확인할 사용자명
     * @return 중복 여부
     */
    boolean existsByUsername(Username username);
    
    /**
     * 사용자를 삭제합니다.
     * 
     * @param id 삭제할 사용자 ID
     */
    void deleteById(UserId id);
}
