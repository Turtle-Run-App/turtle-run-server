package com.turtleRun.be.auth.infrastructure.security;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.Username;
import com.turtleRun.be.auth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security UserDetailsService 구현체
 * 사용자 인증 정보를 제공합니다.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(Username.of(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        
        return new CustomUserPrincipal(user);
    }
    
    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return UserDetails
     */
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(com.turtleRun.be.auth.domain.valueobject.UserId.of(java.util.UUID.fromString(userId)))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
        
        return new CustomUserPrincipal(user);
    }
}
