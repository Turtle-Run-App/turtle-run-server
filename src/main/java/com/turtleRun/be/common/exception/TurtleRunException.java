package com.turtleRun.be.common.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * TurtleRun 애플리케이션의 모든 비즈니스 예외의 기본 클래스
 * 모든 도메인 예외는 이 클래스를 상속받아야 합니다.
 */
@Getter
public abstract class TurtleRunException extends RuntimeException {
    
    /**
     * 에러 코드
     */
    private final ErrorCode errorCode;
    
    /**
     * 예외와 관련된 추가 파라미터들
     */
    private final Map<String, Object> parameters;
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    protected TurtleRunException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = new HashMap<>();
    }
    
    /**
     * 파라미터를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param parameters 추가 파라미터들
     */
    protected TurtleRunException(ErrorCode errorCode, String message, Map<String, Object> parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = new HashMap<>(parameters);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    protected TurtleRunException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = new HashMap<>();
    }
    
    /**
     * 파라미터 추가
     * 
     * @param key 파라미터 키
     * @param value 파라미터 값
     * @return 현재 예외 인스턴스 (메서드 체이닝용)
     */
    public TurtleRunException addParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }
    
    /**
     * 파라미터 맵으로 한번에 추가
     * 
     * @param params 추가할 파라미터들
     * @return 현재 예외 인스턴스 (메서드 체이닝용)
     */
    public TurtleRunException addParameters(Map<String, Object> params) {
        this.parameters.putAll(params);
        return this;
    }
    
    /**
     * HTTP 상태 코드 반환
     * 
     * @return HTTP 상태 코드
     */
    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
