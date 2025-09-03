package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.valueobject.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * JPA 기반 User Entity
 * 인프라스트럭처 레이어에서 도메인 Entity를 JPA와 연결합니다.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_username", columnList = "username"),
    @Index(name = "idx_users_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class UserJpaEntity {
    
    @EmbeddedId
    private UserIdEmbeddable id;
    
    @Embedded
    private NameEmbeddable name;
    
    @Embedded
    private EmailEmbeddable email;
    
    @Embedded
    private UsernameEmbeddable username;
    
    @Embedded
    private PasswordEmbeddable password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "email_verification_status", nullable = false)
    private EmailVerificationStatus emailVerificationStatus;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // JPA용 기본 생성자
    protected UserJpaEntity() {}

    // 도메인 Entity로부터 생성
    public static UserJpaEntity from(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.id = UserIdEmbeddable.from(user.getId());
        entity.name = NameEmbeddable.from(user.getName());
        entity.email = EmailEmbeddable.from(user.getEmail());
        entity.username = UsernameEmbeddable.from(user.getUsername());
        entity.password = PasswordEmbeddable.from(user.getPassword());
        entity.status = user.getStatus();
        entity.emailVerificationStatus = user.getEmailVerificationStatus();
        entity.lastLoginAt = user.getLastLoginAt();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        return entity;
    }

    // 도메인 Entity로 변환
    public User toDomain() {
        User user = new User(
            name.toDomain(),
            email.toDomain(),
            username.toDomain(),
            password.toDomain()
        );
        
        // JPA Entity의 상태를 도메인 Entity에 반영
        // (도메인 Entity는 불변성을 유지하기 위해 리플렉션을 사용하거나 별도 메서드 필요)
        return user;
    }

    // Getters
    public UserIdEmbeddable getId() {
        return id;
    }

    public NameEmbeddable getName() {
        return name;
    }

    public EmailEmbeddable getEmail() {
        return email;
    }

    public UsernameEmbeddable getUsername() {
        return username;
    }

    public PasswordEmbeddable getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public EmailVerificationStatus getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters (JPA용)
    public void setId(UserIdEmbeddable id) {
        this.id = id;
    }

    public void setName(NameEmbeddable name) {
        this.name = name;
    }

    public void setEmail(EmailEmbeddable email) {
        this.email = email;
    }

    public void setUsername(UsernameEmbeddable username) {
        this.username = username;
    }

    public void setPassword(PasswordEmbeddable password) {
        this.password = password;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setEmailVerificationStatus(EmailVerificationStatus emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJpaEntity that = (UserJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserJpaEntity{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", username=" + username +
                ", status=" + status +
                ", emailVerificationStatus=" + emailVerificationStatus +
                '}';
    }
}
