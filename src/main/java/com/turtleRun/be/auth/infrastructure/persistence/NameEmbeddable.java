package com.turtleRun.be.auth.infrastructure.persistence;

import com.turtleRun.be.auth.domain.valueobject.Name;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Name Value Object를 JPA에서 사용하기 위한 Embeddable 클래스
 */
@Embeddable
public class NameEmbeddable implements Serializable {
    
    @Column(name = "name", nullable = false, length = 50)
    private String value;
    
    // JPA용 기본 생성자
    protected NameEmbeddable() {}
    
    public NameEmbeddable(String value) {
        this.value = value;
    }
    
    public static NameEmbeddable from(Name name) {
        return new NameEmbeddable(name.getValue());
    }
    
    public Name toDomain() {
        return Name.of(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameEmbeddable that = (NameEmbeddable) o;
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
