package com.turtleRun.be.auth.domain.valueobject;

/**
 * 사용자 상태 Enum
 * 사용자의 활성/비활성 상태를 나타냅니다.
 */
public enum UserStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    SUSPENDED("정지"),
    DELETED("삭제됨");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
