package com.turtleRun.be.auth.domain.entity;

import com.turtleRun.be.auth.domain.valueobject.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Aggregate Root
 * DDD에서 Aggregate Root는 도메인의 핵심 엔티티로, 비즈니스 규칙을 캡슐화합니다.
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @EmbeddedId
    private UserId id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, length = 50))
    private Name name;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, length = 255))
    private Email email;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username", nullable = false, unique = true, length = 20))
    private Username username;
    
    @Embedded
    @AttributeOverride(name = "hashedValue", column = @Column(name = "password", nullable = false))
    private Password password;
    
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
    protected User() {}

    // 도메인 생성자
    public User(Name name, Email email, Username username, Password password) {
        this.id = UserId.generate();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.status = UserStatus.ACTIVE;
        this.emailVerificationStatus = EmailVerificationStatus.PENDING;
        this.lastLoginAt = null;
    }

    // 비즈니스 메서드들
    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("New password cannot be null");
        }
        this.password = newPassword;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void verifyEmail() {
        if (this.emailVerificationStatus == EmailVerificationStatus.VERIFIED) {
            throw new IllegalStateException("Email is already verified");
        }
        this.emailVerificationStatus = EmailVerificationStatus.VERIFIED;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }
        this.status = UserStatus.INACTIVE;
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public boolean isEmailVerified() {
        return this.emailVerificationStatus == EmailVerificationStatus.VERIFIED;
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", username=" + username +
                ", status=" + status +
                ", emailVerificationStatus=" + emailVerificationStatus +
                '}';
    }
}
