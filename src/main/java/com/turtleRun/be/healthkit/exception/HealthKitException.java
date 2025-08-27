package com.turtleRun.be.healthkit.exception;

import com.turtleRun.be.common.exception.TurtleRunException;
import com.turtleRun.be.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HealthKit Domain의 모든 예외를 관리하는 클래스
 * 데이터 동기화, 검증, 권한 등과 관련된 예외들을 포함합니다.
 */
public class HealthKitException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    protected HealthKitException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    protected HealthKitException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    // ===== 동기화 관련 예외 =====
    
    /**
     * HealthKit 동기화 실패 시 발생하는 예외
     */
    public static class SyncFailed extends HealthKitException {
        public SyncFailed(String reason) {
            super(ErrorCode.HEALTHKIT_SYNC_FAILED, 
                  String.format("HealthKit 동기화에 실패했습니다: %s", reason));
            addParameter("reason", reason);
        }
        
        public SyncFailed(String userId, String reason) {
            super(ErrorCode.HEALTHKIT_SYNC_FAILED, 
                  String.format("사용자 %s의 HealthKit 동기화에 실패했습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public SyncFailed(String userId, LocalDateTime syncTime, String reason) {
            super(ErrorCode.HEALTHKIT_SYNC_FAILED, 
                  String.format("사용자 %s의 HealthKit 동기화 실패 (시간: %s): %s", userId, syncTime, reason));
            addParameter("userId", userId)
                .addParameter("syncTime", syncTime)
                .addParameter("reason", reason);
        }
    }
    
    /**
     * HealthKit 동기화 시간 초과 시 발생하는 예외
     */
    public static class SyncTimeout extends HealthKitException {
        public SyncTimeout(String userId) {
            super(ErrorCode.HEALTHKIT_SYNC_TIMEOUT, 
                  String.format("사용자 %s의 HealthKit 동기화 시간이 초과되었습니다", userId));
            addParameter("userId", userId);
        }
        
        public SyncTimeout(String userId, Integer timeoutSeconds) {
            super(ErrorCode.HEALTHKIT_SYNC_TIMEOUT, 
                  String.format("사용자 %s의 HealthKit 동기화 시간이 초과되었습니다 (제한시간: %d초)", userId, timeoutSeconds));
            addParameter("userId", userId)
                .addParameter("timeoutSeconds", timeoutSeconds);
        }
    }
    
    /**
     * 이미 진행 중인 HealthKit 동기화 시 발생하는 예외
     */
    public static class SyncInProgress extends HealthKitException {
        public SyncInProgress(String userId) {
            super(ErrorCode.HEALTHKIT_SYNC_IN_PROGRESS, 
                  String.format("사용자 %s의 HealthKit 동기화가 이미 진행 중입니다", userId));
            addParameter("userId", userId);
        }
        
        public SyncInProgress(String userId, LocalDateTime startTime) {
            super(ErrorCode.HEALTHKIT_SYNC_IN_PROGRESS, 
                  String.format("사용자 %s의 HealthKit 동기화가 이미 진행 중입니다 (시작시간: %s)", userId, startTime));
            addParameter("userId", userId)
                .addParameter("startTime", startTime);
        }
    }
    
    /**
     * 잘못된 HealthKit 동기화 기간으로 인한 예외
     */
    public static class InvalidSyncPeriod extends HealthKitException {
        public InvalidSyncPeriod(LocalDateTime startTime, LocalDateTime endTime, String reason) {
            super(ErrorCode.HEALTHKIT_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 HealthKit 동기화 기간: %s ~ %s, 이유: %s", startTime, endTime, reason));
            addParameter("startTime", startTime)
                .addParameter("endTime", endTime)
                .addParameter("reason", reason);
        }
        
        public InvalidSyncPeriod(String reason) {
            super(ErrorCode.HEALTHKIT_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 HealthKit 동기화 기간: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    // ===== 데이터 관련 예외 =====
    
    /**
     * 잘못된 HealthKit 데이터로 인한 예외
     */
    public static class InvalidData extends HealthKitException {
        public InvalidData(String dataType, String reason) {
            super(ErrorCode.HEALTHKIT_INVALID_DATA, 
                  String.format("잘못된 HealthKit 데이터 - %s: %s", dataType, reason));
            addParameter("dataType", dataType)
                .addParameter("reason", reason);
        }
        
        public InvalidData(String dataType, Object value, String reason) {
            super(ErrorCode.HEALTHKIT_INVALID_DATA, 
                  String.format("잘못된 HealthKit 데이터 - %s: %s, 이유: %s", dataType, value, reason));
            addParameter("dataType", dataType)
                .addParameter("value", value)
                .addParameter("reason", reason);
        }
        
        public InvalidData(String message) {
            super(ErrorCode.HEALTHKIT_INVALID_DATA, message);
        }
    }
    
    /**
     * HealthKit 데이터 검증 실패 시 발생하는 예외
     */
    public static class DataValidationFailed extends HealthKitException {
        public DataValidationFailed(List<String> errors) {
            super(ErrorCode.HEALTHKIT_VALIDATION_FAILED, 
                  String.format("HealthKit 데이터 검증에 실패했습니다: %s", String.join(", ", errors)));
            addParameter("errors", errors)
                .addParameter("errorCount", errors.size());
        }
        
        public DataValidationFailed(String dataType, List<String> errors) {
            super(ErrorCode.HEALTHKIT_VALIDATION_FAILED, 
                  String.format("HealthKit %s 데이터 검증에 실패했습니다: %s", dataType, String.join(", ", errors)));
            addParameter("dataType", dataType)
                .addParameter("errors", errors)
                .addParameter("errorCount", errors.size());
        }
        
        public DataValidationFailed(String message) {
            super(ErrorCode.HEALTHKIT_VALIDATION_FAILED, message);
        }
    }
    
    /**
     * 손상된 HealthKit 데이터로 인한 예외
     */
    public static class DataCorrupted extends HealthKitException {
        public DataCorrupted(String dataType, String reason) {
            super(ErrorCode.HEALTHKIT_DATA_CORRUPTED, 
                  String.format("HealthKit %s 데이터가 손상되었습니다: %s", dataType, reason));
            addParameter("dataType", dataType)
                .addParameter("reason", reason);
        }
        
        public DataCorrupted(String reason) {
            super(ErrorCode.HEALTHKIT_DATA_CORRUPTED, 
                  String.format("HealthKit 데이터가 손상되었습니다: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 지원하지 않는 HealthKit 데이터 타입으로 인한 예외
     */
    public static class UnsupportedDataType extends HealthKitException {
        public UnsupportedDataType(String dataType) {
            super(ErrorCode.HEALTHKIT_UNSUPPORTED_DATA_TYPE, 
                  String.format("지원하지 않는 HealthKit 데이터 타입입니다: %s", dataType));
            addParameter("dataType", dataType);
        }
        
        public UnsupportedDataType(String dataType, String supportedTypes) {
            super(ErrorCode.HEALTHKIT_UNSUPPORTED_DATA_TYPE, 
                  String.format("지원하지 않는 HealthKit 데이터 타입입니다: %s (지원 타입: %s)", dataType, supportedTypes));
            addParameter("dataType", dataType)
                .addParameter("supportedTypes", supportedTypes);
        }
    }
    
    // ===== 권한 및 가용성 관련 예외 =====
    
    /**
     * HealthKit 접근 권한이 거부된 경우 발생하는 예외
     */
    public static class PermissionDenied extends HealthKitException {
        public PermissionDenied(String userId, String reason) {
            super(ErrorCode.HEALTHKIT_PERMISSION_DENIED, 
                  String.format("사용자 %s의 HealthKit 접근 권한이 거부되었습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public PermissionDenied(String reason) {
            super(ErrorCode.HEALTHKIT_PERMISSION_DENIED, 
                  String.format("HealthKit 접근 권한이 거부되었습니다: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * HealthKit 데이터를 사용할 수 없는 경우 발생하는 예외
     */
    public static class DataNotAvailable extends HealthKitException {
        public DataNotAvailable(String userId, String reason) {
            super(ErrorCode.HEALTHKIT_DATA_NOT_AVAILABLE, 
                  String.format("사용자 %s의 HealthKit 데이터를 사용할 수 없습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public DataNotAvailable(String reason) {
            super(ErrorCode.HEALTHKIT_DATA_NOT_AVAILABLE, 
                  String.format("HealthKit 데이터를 사용할 수 없습니다: %s", reason));
            addParameter("reason", reason);
        }
    }
}
