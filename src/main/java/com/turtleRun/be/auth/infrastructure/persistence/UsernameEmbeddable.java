package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.valueobject.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Username Value Object를 JPA에서 사용하기 위한 Embeddable 클래스
 */
@Embeddable
public class UsernameEmbeddable implements Serializable {
    
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String value;
    
    // JPA용 기본 생성자
    protected UsernameEmbeddable() {}
    
    public UsernameEmbeddable(String value) {
        this.value = value;
    }
    
    public static UsernameEmbeddable from(Username username) {
        return new UsernameEmbeddable(username.getValue());
    }
    
    public Username toDomain() {
        return Username.of(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsernameEmbeddable that = (UsernameEmbeddable) o;
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
