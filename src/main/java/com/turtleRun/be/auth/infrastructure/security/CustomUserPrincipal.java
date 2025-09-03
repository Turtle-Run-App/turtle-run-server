package com.turtleRun.be.auth.infrastructure.security;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails 구현체
 * 도메인 User 엔티티를 Spring Security UserDetails로 변환합니다.
 */
public class CustomUserPrincipal implements UserDetails {
    
    private final User user;
    
    public CustomUserPrincipal(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본적으로 모든 사용자에게 USER 권한을 부여
        // 필요에 따라 사용자 상태나 역할에 따라 다른 권한을 부여할 수 있음
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    @Override
    public String getPassword() {
        return user.getPassword().getHashedValue();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername().getValue();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 기능은 현재 구현하지 않음
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.SUSPENDED;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 기능은 현재 구현하지 않음
    }
    
    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }
    
    /**
     * 도메인 User 엔티티를 반환합니다.
     * 
     * @return User 엔티티
     */
    public User getUser() {
        return user;
    }
    
    /**
     * 사용자 ID를 반환합니다.
     * 
     * @return 사용자 ID
     */
    public String getUserId() {
        return user.getId().getValue().toString();
    }
    
    /**
     * 사용자 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    public String getEmail() {
        return user.getEmail().getValue();
    }
    
    /**
     * 사용자 이름을 반환합니다.
     * 
     * @return 사용자 이름
     */
    public String getName() {
        return user.getName().getValue();
    }
}
