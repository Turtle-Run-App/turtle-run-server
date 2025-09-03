package com.turtleRun.be.auth.infrastructure.repository;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.UserId;
import com.turtleRun.be.auth.domain.valueobject.Username;
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
        // 실제로는 UserJpaEntity를 사용해야 하지만, 현재는 도메인 Entity를 직접 저장
        return jpaUserRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(Username username) {
        return jpaUserRepository.findByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(Username username) {
        return jpaUserRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional
    public void deleteById(UserId id) {
        jpaUserRepository.deleteById(id);
    }
}
