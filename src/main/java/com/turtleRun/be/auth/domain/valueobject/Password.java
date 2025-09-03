package com.turtleRun.be.auth.domain.valueobject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * 비밀번호 Value Object
 * 비밀번호의 유효성을 검증하고 암호화를 처리합니다.
 */
public class Password {
    private final String hashedValue;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * 평문 비밀번호로부터 Password 객체를 생성합니다.
     */
    public static Password of(String plainPassword) {
        validate(plainPassword);
        String hashed = passwordEncoder.encode(plainPassword);
        return new Password(hashed);
    }

    /**
     * 이미 해시된 비밀번호로부터 Password 객체를 생성합니다.
     * (데이터베이스에서 조회할 때 사용)
     */
    public static Password fromHashed(String hashedValue) {
        if (hashedValue == null || hashedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedValue);
    }

    private static void validate(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        if (plainPassword.length() < MIN_LENGTH || plainPassword.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Password length must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }
        
        // 비밀번호 복잡성 검증
        boolean hasLetter = plainPassword.chars().anyMatch(Character::isLetter);
        boolean hasDigit = plainPassword.chars().anyMatch(Character::isDigit);
        
        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("Password must contain at least one letter and one digit");
        }
    }

    /**
     * 평문 비밀번호가 이 해시된 비밀번호와 일치하는지 확인합니다.
     */
    public boolean matches(String plainPassword) {
        return passwordEncoder.matches(plainPassword, hashedValue);
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "[PROTECTED]"; // 보안상 해시값을 직접 노출하지 않음
    }
}
