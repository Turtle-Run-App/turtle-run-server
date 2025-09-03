package com.turtleRun.be.auth.domain.valueobject;

/**
 * 이메일 인증 상태 Enum
 * 사용자의 이메일 인증 상태를 나타냅니다.
 */
public enum EmailVerificationStatus {
    PENDING("인증 대기"),
    VERIFIED("인증 완료"),
    FAILED("인증 실패");

    private final String description;

    EmailVerificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
