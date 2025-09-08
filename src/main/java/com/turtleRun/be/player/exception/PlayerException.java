package com.turtleRun.be.player.exception;

import com.turtleRun.be.common.exception.TurtleRunException;
import com.turtleRun.be.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Player Domain의 모든 예외를 관리하는 클래스
 * 사용자 계정, 캐릭터, 속성 등과 관련된 예외들을 포함합니다.
 */
public class PlayerException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    protected PlayerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    protected PlayerException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    // ===== 사용자 계정 관련 예외 =====
    
    /**
     * 사용자를 찾을 수 없을 때 발생하는 예외
     */
    public static class UserNotFound extends PlayerException {
        public UserNotFound(Long userId) {
            super(ErrorCode.PLAYER_USER_NOT_FOUND, 
                  String.format("사용자를 찾을 수 없습니다: %d", userId));
            addParameter("userId", userId);
        }
        
        public UserNotFound(String userId) {
            super(ErrorCode.PLAYER_USER_NOT_FOUND, 
                  String.format("사용자를 찾을 수 없습니다: %s", userId));
            addParameter("userId", userId);
        }
        
        public UserNotFound(String field, Object value) {
            super(ErrorCode.PLAYER_USER_NOT_FOUND, 
                  String.format("사용자를 찾을 수 없습니다: %s = %s", field, value));
            addParameter("field", field)
                .addParameter("value", value);
        }
    }
    
    /**
     * 잘못된 사용자 데이터로 인한 예외
     */
    public static class InvalidUserData extends PlayerException {
        public InvalidUserData(String field, Object value, String reason) {
            super(ErrorCode.PLAYER_INVALID_USER_DATA, 
                  String.format("잘못된 사용자 데이터 - %s: %s, 이유: %s", field, value, reason));
            addParameter("field", field)
                .addParameter("value", value)
                .addParameter("reason", reason);
        }
        
        public InvalidUserData(String message) {
            super(ErrorCode.PLAYER_INVALID_USER_DATA, message);
        }
    }
    
    /**
     * 사용자 레벨 관련 예외
     */
    public static class LevelInvalid extends PlayerException {
        public LevelInvalid(Integer level, String reason) {
            super(ErrorCode.PLAYER_LEVEL_INVALID, 
                  String.format("잘못된 사용자 레벨: %d, 이유: %s", level, reason));
            addParameter("level", level)
                .addParameter("reason", reason);
        }
        
        public LevelInvalid(String reason) {
            super(ErrorCode.PLAYER_LEVEL_INVALID, 
                  String.format("잘못된 사용자 레벨: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 사용자 경험치 관련 예외
     */
    public static class ExperienceInvalid extends PlayerException {
        public ExperienceInvalid(Integer experience, String reason) {
            super(ErrorCode.PLAYER_EXPERIENCE_INVALID, 
                  String.format("잘못된 사용자 경험치: %d, 이유: %s", experience, reason));
            addParameter("experience", experience)
                .addParameter("reason", reason);
        }
        
        public ExperienceInvalid(String reason) {
            super(ErrorCode.PLAYER_EXPERIENCE_INVALID, 
                  String.format("잘못된 사용자 경험치: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 사용자 코인 관련 예외
     */
    public static class CoinsInvalid extends PlayerException {
        public CoinsInvalid(Integer coins, String reason) {
            super(ErrorCode.PLAYER_COINS_INVALID, 
                  String.format("잘못된 사용자 코인: %d, 이유: %s", coins, reason));
            addParameter("coins", coins)
                .addParameter("reason", reason);
        }
        
        public CoinsInvalid(String reason) {
            super(ErrorCode.PLAYER_COINS_INVALID, 
                  String.format("잘못된 사용자 코인: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    // ===== 캐릭터 관련 예외 =====
    
    /**
     * 캐릭터를 찾을 수 없을 때 발생하는 예외
     */
    public static class CharacterNotFound extends PlayerException {
        public CharacterNotFound(Long characterId) {
            super(ErrorCode.PLAYER_CHARACTER_NOT_FOUND, 
                  String.format("캐릭터를 찾을 수 없습니다: %d", characterId));
            addParameter("characterId", characterId);
        }
        
        public CharacterNotFound(String characterId) {
            super(ErrorCode.PLAYER_CHARACTER_NOT_FOUND, 
                  String.format("캐릭터를 찾을 수 없습니다: %s", characterId));
            addParameter("characterId", characterId);
        }
        
        public CharacterNotFound(Long userId, String characterName) {
            super(ErrorCode.PLAYER_CHARACTER_NOT_FOUND, 
                  String.format("사용자 %d의 캐릭터 '%s'를 찾을 수 없습니다", userId, characterName));
            addParameter("userId", userId)
                .addParameter("characterName", characterName);
        }
    }
    
    /**
     * 잘못된 캐릭터 데이터로 인한 예외
     */
    public static class InvalidCharacterData extends PlayerException {
        public InvalidCharacterData(String field, Object value, String reason) {
            super(ErrorCode.PLAYER_INVALID_CHARACTER_DATA, 
                  String.format("잘못된 캐릭터 데이터 - %s: %s, 이유: %s", field, value, reason));
            addParameter("field", field)
                .addParameter("value", value)
                .addParameter("reason", reason);
        }
        
        public InvalidCharacterData(String message) {
            super(ErrorCode.PLAYER_INVALID_CHARACTER_DATA, message);
        }
    }
    
    // ===== 업적 관련 예외 =====
    
    /**
     * 업적을 찾을 수 없을 때 발생하는 예외
     */
    public static class AchievementNotFound extends PlayerException {
        public AchievementNotFound(Long achievementId) {
            super(ErrorCode.PLAYER_ACHIEVEMENT_NOT_FOUND, 
                  String.format("업적을 찾을 수 없습니다: %d", achievementId));
            addParameter("achievementId", achievementId);
        }
        
        public AchievementNotFound(String achievementName) {
            super(ErrorCode.PLAYER_ACHIEVEMENT_NOT_FOUND, 
                  String.format("업적을 찾을 수 없습니다: %s", achievementName));
            addParameter("achievementName", achievementName);
        }
    }
    
    // ===== 아이템 관련 예외 =====
    
    /**
     * 아이템을 찾을 수 없을 때 발생하는 예외
     */
    public static class ItemNotFound extends PlayerException {
        public ItemNotFound(Long itemId) {
            super(ErrorCode.PLAYER_ITEM_NOT_FOUND, 
                  String.format("아이템을 찾을 수 없습니다: %d", itemId));
            addParameter("itemId", itemId);
        }
        
        public ItemNotFound(String itemName) {
            super(ErrorCode.PLAYER_ITEM_NOT_FOUND, 
                  String.format("아이템을 찾을 수 없습니다: %s", itemName));
            addParameter("itemName", itemName);
        }
    }
    
    // ===== 리소스 관련 예외 =====
    
    /**
     * 리소스가 부족할 때 발생하는 예외
     */
    public static class InsufficientResources extends PlayerException {
        public InsufficientResources(String resourceType, Integer required, Integer available) {
            super(ErrorCode.PLAYER_INSUFFICIENT_RESOURCES, 
                  String.format("리소스가 부족합니다: %s (필요: %d, 보유: %d)", resourceType, required, available));
            addParameter("resourceType", resourceType)
                .addParameter("required", required)
                .addParameter("available", available);
        }
        
        public InsufficientResources(String resourceType, String reason) {
            super(ErrorCode.PLAYER_INSUFFICIENT_RESOURCES, 
                  String.format("리소스가 부족합니다: %s, 이유: %s", resourceType, reason));
            addParameter("resourceType", resourceType)
                .addParameter("reason", reason);
        }
        
        public InsufficientResources(String message) {
            super(ErrorCode.PLAYER_INSUFFICIENT_RESOURCES, message);
        }
    }
    
    // ===== 계정 상태 관련 예외 =====
    
    /**
     * 계정이 비활성화된 경우 발생하는 예외
     */
    public static class AccountInactive extends PlayerException {
        public AccountInactive(Long userId, String reason) {
            super(ErrorCode.FORBIDDEN, 
                  String.format("사용자 %d의 계정이 비활성화되었습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public AccountInactive(String reason) {
            super(ErrorCode.FORBIDDEN, 
                  String.format("계정이 비활성화되었습니다: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 계정이 일시정지된 경우 발생하는 예외
     */
    public static class AccountSuspended extends PlayerException {
        public AccountSuspended(Long userId, LocalDateTime suspensionEnd) {
            super(ErrorCode.FORBIDDEN, 
                  String.format("사용자 %d의 계정이 일시정지되었습니다 (해제시간: %s)", userId, suspensionEnd));
            addParameter("userId", userId)
                .addParameter("suspensionEnd", suspensionEnd);
        }
        
        public AccountSuspended(Long userId, String reason) {
            super(ErrorCode.FORBIDDEN, 
                  String.format("사용자 %d의 계정이 일시정지되었습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
    }
}

