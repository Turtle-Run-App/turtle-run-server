package com.turtleRun.be.auth.domain.valueobject;

import java.util.Objects;

/**
 * 사용자 이름 Value Object
 * 이름의 유효성을 검증하고 불변성을 보장합니다.
 */
public class Name {
    private final String value;
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z\\s]+$";

    private Name(String value) {
        validate(value);
        this.value = value.trim();
    }

    public static Name of(String value) {
        return new Name(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Name length must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }
        
        if (!trimmed.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("Name can only contain Korean characters, English letters, and spaces");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
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
