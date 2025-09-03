package com.turtleRun.be.auth.domain.valueobject;

import java.util.Objects;

/**
 * 사용자명 Value Object
 * 사용자명의 유효성을 검증하고 불변성을 보장합니다.
 */
public class Username {
    private final String value;
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";

    private Username(String value) {
        validate(value);
        this.value = value.toLowerCase().trim();
    }

    public static Username of(String value) {
        return new Username(value);
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Username length must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }
        
        if (!trimmed.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
        }
        
        // 첫 글자는 문자여야 함
        if (!Character.isLetter(trimmed.charAt(0))) {
            throw new IllegalArgumentException("Username must start with a letter");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
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
