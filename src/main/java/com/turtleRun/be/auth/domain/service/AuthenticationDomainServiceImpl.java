package com.turtleRun.be.auth.domain.service;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Email;
import com.turtleRun.be.auth.domain.valueobject.Password;
import com.turtleRun.be.auth.domain.valueobject.Username;
import com.turtleRun.be.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 도메인 서비스 구현체
 * 실제 비즈니스 로직을 구현합니다.
 */
@Service
@Transactional(readOnly = true)
public class AuthenticationDomainServiceImpl implements AuthenticationDomainService {
    
    private final UserRepository userRepository;
    
    public AuthenticationDomainServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public boolean authenticate(User user, String plainPassword) {
        if (user == null) {
            return false;
        }
        
        if (!user.isActive()) {
            return false;
        }
        
        return user.getPassword().matches(plainPassword);
    }
    
    @Override
    public boolean isEmailDuplicate(Email email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean isUsernameDuplicate(Username username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional
    public User register(User user) {
        // 이메일 중복 확인
        if (isEmailDuplicate(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail().getValue());
        }
        
        // 사용자명 중복 확인
        if (isUsernameDuplicate(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername().getValue());
        }
        
        // 사용자 저장
        return userRepository.save(user);
    }
}
