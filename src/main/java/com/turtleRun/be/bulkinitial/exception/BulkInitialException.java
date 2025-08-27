package com.turtleRun.be.bulkinitial.exception;

import com.turtleRun.be.common.exception.TurtleRunException;
import com.turtleRun.be.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bulk Initial Domain의 모든 예외를 관리하는 클래스
 * 신규 사용자의 러닝 데이터 초기 동기화와 관련된 예외들을 포함합니다.
 */
public class BulkInitialException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    protected BulkInitialException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    protected BulkInitialException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    // ===== 동기화 한도 관련 예외 =====
    
    /**
     * 동기화 한도를 초과했을 때 발생하는 예외
     */
    public static class SyncLimitExceeded extends BulkInitialException {
        public SyncLimitExceeded(Integer limit, Integer requested) {
            super(ErrorCode.BULK_INITIAL_SYNC_LIMIT_EXCEEDED, 
                  String.format("동기화 한도를 초과했습니다: 요청 %d, 한도 %d", requested, limit));
            addParameter("limit", limit)
                .addParameter("requested", requested);
        }
        
        public SyncLimitExceeded(String limitType, Integer limit, Integer requested) {
            super(ErrorCode.BULK_INITIAL_SYNC_LIMIT_EXCEEDED, 
                  String.format("%s 동기화 한도를 초과했습니다: 요청 %d, 한도 %d", limitType, requested, limit));
            addParameter("limitType", limitType)
                .addParameter("limit", limit)
                .addParameter("requested", requested);
        }
        
        public SyncLimitExceeded(String message) {
            super(ErrorCode.BULK_INITIAL_SYNC_LIMIT_EXCEEDED, message);
        }
    }
    
    // ===== 동기화 진행 상태 관련 예외 =====
    
    /**
     * 이미 진행 중인 동기화 시 발생하는 예외
     */
    public static class SyncInProgress extends BulkInitialException {
        public SyncInProgress(Long userId) {
            super(ErrorCode.BULK_INITIAL_SYNC_IN_PROGRESS, 
                  String.format("사용자 %d의 동기화가 이미 진행 중입니다", userId));
            addParameter("userId", userId);
        }
        
        public SyncInProgress(Long userId, LocalDateTime startTime) {
            super(ErrorCode.BULK_INITIAL_SYNC_IN_PROGRESS, 
                  String.format("사용자 %d의 동기화가 이미 진행 중입니다 (시작시간: %s)", userId, startTime));
            addParameter("userId", userId)
                .addParameter("startTime", startTime);
        }
        
        public SyncInProgress(Long userId, String syncId) {
            super(ErrorCode.BULK_INITIAL_SYNC_IN_PROGRESS, 
                  String.format("사용자 %d의 동기화가 이미 진행 중입니다 (동기화 ID: %s)", userId, syncId));
            addParameter("userId", userId)
                .addParameter("syncId", syncId);
        }
    }
    
    // ===== 동기화 기간 관련 예외 =====
    
    /**
     * 잘못된 동기화 기간으로 인한 예외
     */
    public static class InvalidSyncPeriod extends BulkInitialException {
        public InvalidSyncPeriod(LocalDateTime startTime, LocalDateTime endTime, String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 동기화 기간: %s ~ %s, 이유: %s", startTime, endTime, reason));
            addParameter("startTime", startTime)
                .addParameter("endTime", endTime)
                .addParameter("reason", reason);
        }
        
        public InvalidSyncPeriod(String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 동기화 기간: %s", reason));
            addParameter("reason", reason);
        }
        
        public InvalidSyncPeriod(LocalDateTime startTime, LocalDateTime endTime) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 동기화 기간: %s ~ %s", startTime, endTime));
            addParameter("startTime", startTime)
                .addParameter("endTime", endTime);
        }
    }
    
    // ===== 데이터 검증 관련 예외 =====
    
    /**
     * 잘못된 데이터로 인한 예외
     */
    public static class InvalidData extends BulkInitialException {
        public InvalidData(String message) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, message);
        }
        
        public InvalidData(String field, Object value) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 데이터 - %s: %s", field, value));
            addParameter("field", field)
                .addParameter("value", value);
        }
        
        public InvalidData(String field, Object value, String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_SYNC_PERIOD, 
                  String.format("잘못된 데이터 - %s: %s, 이유: %s", field, value, reason));
            addParameter("field", field)
                .addParameter("value", value)
                .addParameter("reason", reason);
        }
    }
    
    // ===== 동기화 실패 관련 예외 =====
    
    /**
     * 대량 동기화 실패 시 발생하는 예외
     */
    public static class SyncFailed extends BulkInitialException {
        public SyncFailed(Long userId, String reason) {
            super(ErrorCode.BULK_INITIAL_SYNC_FAILED, 
                  String.format("사용자 %d의 대량 동기화에 실패했습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public SyncFailed(Long userId, LocalDateTime syncTime, String reason) {
            super(ErrorCode.BULK_INITIAL_SYNC_FAILED, 
                  String.format("사용자 %d의 대량 동기화 실패 (시간: %s): %s", userId, syncTime, reason));
            addParameter("userId", userId)
                .addParameter("syncTime", syncTime)
                .addParameter("reason", reason);
        }
        
        public SyncFailed(String reason) {
            super(ErrorCode.BULK_INITIAL_SYNC_FAILED, 
                  String.format("대량 동기화에 실패했습니다: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    // ===== 데이터 크기 관련 예외 =====
    
    /**
     * 동기화할 데이터가 너무 클 때 발생하는 예외
     */
    public static class DataTooLarge extends BulkInitialException {
        public DataTooLarge(Long dataSize, Long maxSize, String unit) {
            super(ErrorCode.BULK_INITIAL_DATA_TOO_LARGE, 
                  String.format("동기화할 데이터가 너무 큽니다: %d%s (최대: %d%s)", dataSize, unit, maxSize, unit));
            addParameter("dataSize", dataSize)
                .addParameter("maxSize", maxSize)
                .addParameter("unit", unit);
        }
        
        public DataTooLarge(Long dataSize, String unit) {
            super(ErrorCode.BULK_INITIAL_DATA_TOO_LARGE, 
                  String.format("동기화할 데이터가 너무 큽니다: %d%s", dataSize, unit));
            addParameter("dataSize", dataSize)
                .addParameter("unit", unit);
        }
        
        public DataTooLarge(String message) {
            super(ErrorCode.BULK_INITIAL_DATA_TOO_LARGE, message);
        }
    }
    
    /**
     * 잘못된 배치 크기로 인한 예외
     */
    public static class InvalidBatchSize extends BulkInitialException {
        public InvalidBatchSize(Integer batchSize, Integer minSize, Integer maxSize) {
            super(ErrorCode.BULK_INITIAL_INVALID_BATCH_SIZE, 
                  String.format("잘못된 배치 크기: %d (범위: %d ~ %d)", batchSize, minSize, maxSize));
            addParameter("batchSize", batchSize)
                .addParameter("minSize", minSize)
                .addParameter("maxSize", maxSize);
        }
        
        public InvalidBatchSize(Integer batchSize, String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_BATCH_SIZE, 
                  String.format("잘못된 배치 크기: %d, 이유: %s", batchSize, reason));
            addParameter("batchSize", batchSize)
                .addParameter("reason", reason);
        }
        
        public InvalidBatchSize(String message) {
            super(ErrorCode.BULK_INITIAL_INVALID_BATCH_SIZE, message);
        }
    }
    
    // ===== 동기화 시간 관련 예외 =====
    
    /**
     * 대량 동기화 시간 초과 시 발생하는 예외
     */
    public static class SyncTimeout extends BulkInitialException {
        public SyncTimeout(Long userId, Integer timeoutSeconds) {
            super(ErrorCode.BULK_INITIAL_SYNC_TIMEOUT, 
                  String.format("사용자 %d의 대량 동기화 시간이 초과되었습니다 (제한시간: %d초)", userId, timeoutSeconds));
            addParameter("userId", userId)
                .addParameter("timeoutSeconds", timeoutSeconds);
        }
        
        public SyncTimeout(Long userId, LocalDateTime startTime, Integer timeoutSeconds) {
            super(ErrorCode.BULK_INITIAL_SYNC_TIMEOUT, 
                  String.format("사용자 %d의 대량 동기화 시간 초과 (시작: %s, 제한: %d초)", userId, startTime, timeoutSeconds));
            addParameter("userId", userId)
                .addParameter("startTime", startTime)
                .addParameter("timeoutSeconds", timeoutSeconds);
        }
        
        public SyncTimeout(String message) {
            super(ErrorCode.BULK_INITIAL_SYNC_TIMEOUT, message);
        }
    }
    
    // ===== 부분 성공 관련 예외 =====
    
    /**
     * 일부 데이터만 동기화된 경우 발생하는 예외
     */
    public static class PartialSuccess extends BulkInitialException {
        public PartialSuccess(Long userId, Integer totalCount, Integer successCount, Integer failedCount) {
            super(ErrorCode.BULK_INITIAL_PARTIAL_SUCCESS, 
                  String.format("사용자 %d의 동기화가 부분적으로 성공했습니다: 총 %d개 중 %d개 성공, %d개 실패", 
                              userId, totalCount, successCount, failedCount));
            addParameter("userId", userId)
                .addParameter("totalCount", totalCount)
                .addParameter("successCount", successCount)
                .addParameter("failedCount", failedCount);
        }
        
        public PartialSuccess(String message) {
            super(ErrorCode.BULK_INITIAL_PARTIAL_SUCCESS, message);
        }
    }
    
    // ===== 중복 데이터 관련 예외 =====
    
    /**
     * 중복된 데이터가 포함된 경우 발생하는 예외
     */
    public static class DuplicateData extends BulkInitialException {
        public DuplicateData(Long userId, Integer duplicateCount) {
            super(ErrorCode.BULK_INITIAL_DUPLICATE_DATA, 
                  String.format("사용자 %d의 동기화 데이터에 중복이 포함되어 있습니다: %d개", userId, duplicateCount));
            addParameter("userId", userId)
                .addParameter("duplicateCount", duplicateCount);
        }
        
        public DuplicateData(Long userId, List<String> duplicateIds) {
            super(ErrorCode.BULK_INITIAL_DUPLICATE_DATA, 
                  String.format("사용자 %d의 동기화 데이터에 중복이 포함되어 있습니다: %s", userId, String.join(", ", duplicateIds)));
            addParameter("userId", userId)
                .addParameter("duplicateIds", duplicateIds)
                .addParameter("duplicateCount", duplicateIds.size());
        }
        
        public DuplicateData(String message) {
            super(ErrorCode.BULK_INITIAL_DUPLICATE_DATA, message);
        }
    }
    
    // ===== 사용자 상태 관련 예외 =====
    
    /**
     * 사용자 상태가 동기화에 적합하지 않은 경우 발생하는 예외
     */
    public static class InvalidUserStatus extends BulkInitialException {
        public InvalidUserStatus(Long userId, String currentStatus, String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_USER_STATUS, 
                  String.format("사용자 %d의 상태가 동기화에 적합하지 않습니다: 현재 상태 %s, 이유: %s", 
                              userId, currentStatus, reason));
            addParameter("userId", userId)
                .addParameter("currentStatus", currentStatus)
                .addParameter("reason", reason);
        }
        
        public InvalidUserStatus(Long userId, String reason) {
            super(ErrorCode.BULK_INITIAL_INVALID_USER_STATUS, 
                  String.format("사용자 %d의 상태가 동기화에 적합하지 않습니다: %s", userId, reason));
            addParameter("userId", userId)
                .addParameter("reason", reason);
        }
        
        public InvalidUserStatus(String message) {
            super(ErrorCode.BULK_INITIAL_INVALID_USER_STATUS, message);
        }
    }
}
