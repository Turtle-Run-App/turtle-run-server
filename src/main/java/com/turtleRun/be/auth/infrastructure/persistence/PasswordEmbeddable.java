package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.valueobject.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Password Value Object를 JPA에서 사용하기 위한 Embeddable 클래스
 */
@Embeddable
public class PasswordEmbeddable implements Serializable {
    
    @Column(name = "password", nullable = false, length = 255)
    private String hashedValue;
    
    // JPA용 기본 생성자
    protected PasswordEmbeddable() {}
    
    public PasswordEmbeddable(String hashedValue) {
        this.hashedValue = hashedValue;
    }
    
    public static PasswordEmbeddable from(Password password) {
        return new PasswordEmbeddable(password.getHashedValue());
    }
    
    public Password toDomain() {
        return Password.fromHashed(hashedValue);
    }
    
    public String getHashedValue() {
        return hashedValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordEmbeddable that = (PasswordEmbeddable) o;
        return Objects.equals(hashedValue, that.hashedValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }
    
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
