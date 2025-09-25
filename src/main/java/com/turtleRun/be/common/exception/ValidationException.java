package com.turtleRun.be.common.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 데이터 검증에 실패했을 때 발생하는 예외
 * 필드별 검증 오류 정보를 포함할 수 있습니다.
 */
@Getter
public class ValidationException extends TurtleRunException {
    
    /**
     * 검증 오류 목록
     */
    private final List<FieldError> fieldErrors;
    
    /**
     * 기본 생성자
     * 
     * @param message 검증 실패 메시지
     */
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
        this.fieldErrors = new ArrayList<>();
    }
    
    /**
     * 필드 오류를 포함한 생성자
     * 
     * @param message 검증 실패 메시지
     * @param fieldErrors 필드별 오류 목록
     */
    public ValidationException(String message, List<FieldError> fieldErrors) {
        super(ErrorCode.VALIDATION_FAILED, message);
        this.fieldErrors = fieldErrors != null ? fieldErrors : new ArrayList<>();
    }
    
    /**
     * 단일 필드 오류를 포함한 생성자
     * 
     * @param fieldName 필드명
     * @param fieldValue 필드값
     * @param reason 오류 이유
     */
    public ValidationException(String fieldName, Object fieldValue, String reason) {
        super(ErrorCode.VALIDATION_FAILED, 
              String.format("필드 '%s' 검증 실패: %s", fieldName, reason));
        
        this.fieldErrors = new ArrayList<>();
        this.fieldErrors.add(new FieldError(fieldName, fieldValue, reason));
        
        // 파라미터 추가
        addParameter("fieldName", fieldName)
            .addParameter("fieldValue", fieldValue)
            .addParameter("reason", reason);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param message 검증 실패 메시지
     * @param cause 원인 예외
     */
    public ValidationException(String message, Throwable cause) {
        super(ErrorCode.VALIDATION_FAILED, message, cause);
        this.fieldErrors = new ArrayList<>();
    }
    
    /**
     * 필드 오류 추가
     * 
     * @param fieldError 필드 오류
     * @return 현재 예외 인스턴스 (메서드 체이닝용)
     */
    public ValidationException addFieldError(FieldError fieldError) {
        this.fieldErrors.add(fieldError);
        return this;
    }
    
    /**
     * 필드 오류 추가 (편의 메서드)
     * 
     * @param fieldName 필드명
     * @param fieldValue 필드값
     * @param reason 오류 이유
     * @return 현재 예외 인스턴스 (메서드 체이닝용)
     */
    public ValidationException addFieldError(String fieldName, Object fieldValue, String reason) {
        this.fieldErrors.add(new FieldError(fieldName, fieldValue, reason));
        return this;
    }
    
    /**
     * 필드 오류가 있는지 확인
     * 
     * @return 필드 오류가 있으면 true
     */
    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
    
    /**
     * 필드 오류 개수 반환
     * 
     * @return 필드 오류 개수
     */
    public int getFieldErrorCount() {
        return fieldErrors.size();
    }
    
    /**
     * 필드별 검증 오류 정보를 담는 내부 클래스
     */
    @Getter
    public static class FieldError {
        /**
         * 필드명
         */
        private final String fieldName;
        
        /**
         * 필드값
         */
        private final Object fieldValue;
        
        /**
         * 오류 이유
         */
        private final String reason;
        
        /**
         * 생성자
         * 
         * @param fieldName 필드명
         * @param fieldValue 필드값
         * @param reason 오류 이유
         */
        public FieldError(String fieldName, Object fieldValue, String reason) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.reason = reason;
        }
    }
}

