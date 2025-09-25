package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.valueobject.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Email Value Object를 JPA에서 사용하기 위한 Embeddable 클래스
 */
@Embeddable
public class EmailEmbeddable implements Serializable {
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String value;
    
    // JPA용 기본 생성자
    protected EmailEmbeddable() {}
    
    public EmailEmbeddable(String value) {
        this.value = value;
    }
    
    public static EmailEmbeddable from(Email email) {
        return new EmailEmbeddable(email.getValue());
    }
    
    public Email toDomain() {
        return Email.of(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailEmbeddable that = (EmailEmbeddable) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
