package com.turtleRun.be.auth.infrastructure.repository;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.UserId;
import com.turtleRun.be.auth.domain.valueobject.Username;
import com.turtleRun.be.auth.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA 기반 사용자 저장소 구현체
 * 인프라스트럭처 레이어에서 도메인 Repository 인터페이스를 구현합니다.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>, UserRepository {
    
    @Override
    @Query("SELECT u FROM User u WHERE u.email.value = :emailValue")
    Optional<User> findByEmail(@Param("emailValue") String emailValue);
    
    @Override
    @Query("SELECT u FROM User u WHERE u.username.value = :usernameValue")
    Optional<User> findByUsername(@Param("usernameValue") String usernameValue);
    
    @Override
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email.value = :emailValue")
    boolean existsByEmail(@Param("emailValue") String emailValue);
    
    @Override
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username.value = :usernameValue")
    boolean existsByUsername(@Param("usernameValue") String usernameValue);
    
    // 도메인 Repository 인터페이스의 메서드들을 구현
    @Override
    default Optional<User> findByEmail(Email email) {
        return findByEmail(email.getValue());
    }
    
    @Override
    default Optional<User> findByUsername(Username username) {
        return findByUsername(username.getValue());
    }
    
    @Override
    default boolean existsByEmail(Email email) {
        return existsByEmail(email.getValue());
    }
    
    @Override
    default boolean existsByUsername(Username username) {
        return existsByUsername(username.getValue());
    }
    
    @Override
    default Optional<User> findById(UserId id) {
        return findById(id.getValue());
    }
    
    @Override
    default void deleteById(UserId id) {
        deleteById(id.getValue());
    }
}
