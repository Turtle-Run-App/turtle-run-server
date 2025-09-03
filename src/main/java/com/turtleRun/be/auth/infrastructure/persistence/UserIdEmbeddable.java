package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.valueobject.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * UserId Value Object를 JPA에서 사용하기 위한 Embeddable 클래스
 * 인프라스트럭처 레이어에서 도메인 Value Object를 JPA와 연결합니다.
 */
@Embeddable
public class UserIdEmbeddable implements Serializable {
    
    @Column(name = "id", nullable = false, updatable = false)
    private UUID value;
    
    // JPA용 기본 생성자
    protected UserIdEmbeddable() {}
    
    public UserIdEmbeddable(UUID value) {
        this.value = value;
    }
    
    public static UserIdEmbeddable from(UserId userId) {
        return new UserIdEmbeddable(userId.getValue());
    }
    
    public UserId toDomain() {
        return UserId.of(value);
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdEmbeddable that = (UserIdEmbeddable) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
