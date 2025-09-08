package com.turtleRun.be.auth.infrastructure.repository;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.UserId;
import com.turtleRun.be.auth.domain.valueobject.Username;
import com.turtleRun.be.auth.infrastructure.persistence.UserJpaEntity;
import com.turtleRun.be.auth.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserRepository 구현체
 * 인프라스트럭처 레이어에서 도메인 Repository 인터페이스를 구현합니다.
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    
    private final JpaUserRepository jpaUserRepository;
    
    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }
    
    @Override
    @Transactional
    public User save(User user) {
        // 도메인 Entity를 JPA Entity로 변환하여 저장
        UserJpaEntity jpaEntity = toJpaEntity(user);
        UserJpaEntity savedEntity = jpaUserRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.getValue())
                .map(this::toDomainEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmailValue(email.getValue())
                .map(this::toDomainEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(Username username) {
        return jpaUserRepository.findByUsernameValue(username.getValue())
                .map(this::toDomainEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmailValue(email.getValue());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(Username username) {
        return jpaUserRepository.existsByUsernameValue(username.getValue());
    }
    
    @Override
    @Transactional
    public void deleteById(UserId id) {
        jpaUserRepository.deleteById(id.getValue());
    }
    
    /**
     * 도메인 Entity를 JPA Entity로 변환
     */
    private UserJpaEntity toJpaEntity(User user) {
        // TODO: 실제 매핑 로직 구현 필요
        // 현재는 간단한 예시로 처리
        return new UserJpaEntity();
    }
    
    /**
     * JPA Entity를 도메인 Entity로 변환
     */
    private User toDomainEntity(UserJpaEntity jpaEntity) {
        // TODO: 실제 매핑 로직 구현 필요
        // 현재는 간단한 예시로 처리
        return null;
    }
}
