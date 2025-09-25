package com.turtleRun.be.auth.infrastructure.repository;

import com.turtleRun.be.auth.infrastructure.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA 기반 사용자 저장소 구현체
 * 인프라스트럭처 레이어에서 JPA 엔티티를 관리합니다.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    
    @Query("SELECT u FROM UserJpaEntity u WHERE u.email.value = :emailValue")
    Optional<UserJpaEntity> findByEmailValue(@Param("emailValue") String emailValue);
    
    @Query("SELECT u FROM UserJpaEntity u WHERE u.username.value = :usernameValue")
    Optional<UserJpaEntity> findByUsernameValue(@Param("usernameValue") String usernameValue);
    
    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.email.value = :emailValue")
    boolean existsByEmailValue(@Param("emailValue") String emailValue);
    
    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.username.value = :usernameValue")
    boolean existsByUsernameValue(@Param("usernameValue") String usernameValue);
}
