package com.turtleRun.be.running.exception;

import com.turtleRun.be.common.exception.TurtleRunException;
import com.turtleRun.be.common.exception.ErrorCode;

/**
 * Running Domain의 모든 예외를 관리하는 클래스
 * 러닝 세션, 심박수, 경로, 구간 등과 관련된 예외들을 포함합니다.
 */
public class RunningException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    protected RunningException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    protected RunningException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    // ===== 러닝 세션 관련 예외 =====
    
    /**
     * 러닝 세션을 찾을 수 없을 때 발생하는 예외
     */
    public static class SessionNotFound extends RunningException {
        public SessionNotFound(Long sessionId) {
            super(ErrorCode.RUNNING_SESSION_NOT_FOUND, 
                  String.format("러닝 세션을 찾을 수 없습니다: %d", sessionId));
            addParameter("sessionId", sessionId);
        }
        
        public SessionNotFound(String sessionId) {
            super(ErrorCode.RUNNING_SESSION_NOT_FOUND, 
                  String.format("러닝 세션을 찾을 수 없습니다: %s", sessionId));
            addParameter("sessionId", sessionId);
        }
    }
    
    /**
     * 이미 완료된 러닝 세션에 대한 작업 시 발생하는 예외
     */
    public static class SessionAlreadyCompleted extends RunningException {
        public SessionAlreadyCompleted(Long sessionId) {
            super(ErrorCode.RUNNING_SESSION_ALREADY_COMPLETED, 
                  String.format("이미 완료된 러닝 세션입니다: %d", sessionId));
            addParameter("sessionId", sessionId);
        }
        
        public SessionAlreadyCompleted(String sessionId) {
            super(ErrorCode.RUNNING_SESSION_ALREADY_COMPLETED, 
                  String.format("이미 완료된 러닝 세션입니다: %s", sessionId));
            addParameter("sessionId", sessionId);
        }
    }
    
    /**
     * 잘못된 러닝 세션 데이터로 인한 예외
     */
    public static class SessionInvalidData extends RunningException {
        public SessionInvalidData(String message) {
            super(ErrorCode.RUNNING_SESSION_INVALID_DATA, message);
        }
        
        public SessionInvalidData(String field, Object value) {
            super(ErrorCode.RUNNING_SESSION_INVALID_DATA, 
                  String.format("잘못된 러닝 세션 데이터 - %s: %s", field, value));
            addParameter("field", field)
                .addParameter("value", value);
        }
    }
    
    /**
     * 진행 중인 러닝 세션에 대한 작업 시 발생하는 예외
     */
    public static class SessionInProgress extends RunningException {
        public SessionInProgress(Long sessionId) {
            super(ErrorCode.RUNNING_SESSION_IN_PROGRESS, 
                  String.format("진행 중인 러닝 세션입니다: %d", sessionId));
            addParameter("sessionId", sessionId);
        }
        
        public SessionInProgress(String sessionId) {
            super(ErrorCode.RUNNING_SESSION_IN_PROGRESS, 
                  String.format("진행 중인 러닝 세션입니다: %s", sessionId));
            addParameter("sessionId", sessionId);
        }
    }
    
    /**
     * 러닝 세션 시간 관련 예외
     */
    public static class DurationInvalid extends RunningException {
        public DurationInvalid(Integer duration, String reason) {
            super(ErrorCode.RUNNING_SESSION_DURATION_INVALID, 
                  String.format("잘못된 러닝 세션 시간: %d, 이유: %s", duration, reason));
            addParameter("duration", duration)
                .addParameter("reason", reason);
        }
        
        public DurationInvalid(String reason) {
            super(ErrorCode.RUNNING_SESSION_DURATION_INVALID, 
                  String.format("잘못된 러닝 세션 시간: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 러닝 세션 거리 관련 예외
     */
    public static class DistanceInvalid extends RunningException {
        public DistanceInvalid(java.math.BigDecimal distance, String reason) {
            super(ErrorCode.RUNNING_SESSION_DISTANCE_INVALID, 
                  String.format("잘못된 러닝 세션 거리: %s, 이유: %s", distance, reason));
            addParameter("distance", distance)
                .addParameter("reason", reason);
        }
        
        public DistanceInvalid(String reason) {
            super(ErrorCode.RUNNING_SESSION_DISTANCE_INVALID, 
                  String.format("잘못된 러닝 세션 거리: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    /**
     * 러닝 세션 칼로리 관련 예외
     */
    public static class CaloriesInvalid extends RunningException {
        public CaloriesInvalid(Integer calories, String reason) {
            super(ErrorCode.RUNNING_SESSION_CALORIES_INVALID, 
                  String.format("잘못된 러닝 세션 칼로리: %d, 이유: %s", calories, reason));
            addParameter("calories", calories)
                .addParameter("reason", reason);
        }
        
        public CaloriesInvalid(String reason) {
            super(ErrorCode.RUNNING_SESSION_CALORIES_INVALID, 
                  String.format("잘못된 러닝 세션 칼로리: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    // ===== 심박수 데이터 관련 예외 =====
    
    /**
     * 잘못된 심박수 데이터로 인한 예외
     */
    public static class HeartRateInvalid extends RunningException {
        public HeartRateInvalid(Integer heartRate, String reason) {
            super(ErrorCode.RUNNING_HEART_RATE_INVALID, 
                  String.format("잘못된 심박수 데이터: %d bpm, 이유: %s", heartRate, reason));
            addParameter("heartRate", heartRate)
                .addParameter("reason", reason);
        }
        
        public HeartRateInvalid(String reason) {
            super(ErrorCode.RUNNING_HEART_RATE_INVALID, 
                  String.format("잘못된 심박수 데이터: %s", reason));
            addParameter("reason", reason);
        }
    }
    
    // ===== 경로 데이터 관련 예외 =====
    
    /**
     * 잘못된 경로 데이터로 인한 예외
     */
    public static class RoutePointInvalid extends RunningException {
        public RoutePointInvalid(String reason) {
            super(ErrorCode.RUNNING_ROUTE_POINT_INVALID, 
                  String.format("잘못된 경로 데이터: %s", reason));
            addParameter("reason", reason);
        }
        
        public RoutePointInvalid(Double latitude, Double longitude, String reason) {
            super(ErrorCode.RUNNING_ROUTE_POINT_INVALID, 
                  String.format("잘못된 경로 데이터: 위도 %.6f, 경도 %.6f, 이유: %s", latitude, longitude, reason));
            addParameter("latitude", latitude)
                .addParameter("longitude", longitude)
                .addParameter("reason", reason);
        }
    }
    
    // ===== 구간 데이터 관련 예외 =====
    
    /**
     * 잘못된 구간 데이터로 인한 예외
     */
    public static class SplitInvalid extends RunningException {
        public SplitInvalid(String reason) {
            super(ErrorCode.RUNNING_SPLIT_INVALID, 
                  String.format("잘못된 구간 데이터: %s", reason));
            addParameter("reason", reason);
        }
        
        public SplitInvalid(Integer splitNumber, String reason) {
            super(ErrorCode.RUNNING_SPLIT_INVALID, 
                  String.format("잘못된 구간 데이터 (구간 %d): %s", splitNumber, reason));
            addParameter("splitNumber", splitNumber)
                .addParameter("reason", reason);
        }
    }
}
