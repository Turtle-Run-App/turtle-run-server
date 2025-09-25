package com.turtleRun.be.common.exception;

/**
 * 일반적인 비즈니스 로직 오류에 사용하는 예외
 * 특정 도메인에 속하지 않는 비즈니스 규칙 위반 시 사용합니다.
 */
public class BusinessException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * 잘못된 요청 예외 생성 (편의 메서드)
     * 
     * @param message 에러 메시지
     * @return BusinessException 인스턴스
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(ErrorCode.INVALID_INPUT, message);
    }
    
    /**
     * 잘못된 입력값 예외 생성 (편의 메서드)
     * 
     * @param fieldName 필드명
     * @param fieldValue 필드값
     * @param reason 오류 이유
     * @return BusinessException 인스턴스
     */
    public static BusinessException invalidInput(String fieldName, Object fieldValue, String reason) {
        BusinessException exception = new BusinessException(ErrorCode.INVALID_INPUT, 
                                   String.format("잘못된 입력값: %s = %s, 이유: %s", fieldName, fieldValue, reason));
        exception.addParameter("fieldName", fieldName)
                .addParameter("fieldValue", fieldValue)
                .addParameter("reason", reason);
        return exception;
    }
    
    /**
     * 중복 리소스 예외 생성 (편의 메서드)
     * 
     * @param resourceType 리소스 타입
     * @param resourceId 리소스 ID
     * @return BusinessException 인스턴스
     */
    public static BusinessException duplicateResource(String resourceType, Object resourceId) {
        BusinessException exception = new BusinessException(ErrorCode.DUPLICATE_RESOURCE, 
                                   String.format("중복된 %s입니다: %s", resourceType, resourceId));
        exception.addParameter("resourceType", resourceType)
                .addParameter("resourceId", resourceId);
        return exception;
    }
    
    /**
     * 리소스 사용 중 예외 생성 (편의 메서드)
     * 
     * @param resourceType 리소스 타입
     * @param resourceId 리소스 ID
     * @return BusinessException 인스턴스
     */
    public static BusinessException resourceInUse(String resourceType, Object resourceId) {
        BusinessException exception = new BusinessException(ErrorCode.RESOURCE_IN_USE, 
                                   String.format("%s이(가) 사용 중입니다: %s", resourceType, resourceId));
        exception.addParameter("resourceId", resourceId);
        exception.addParameter("resourceType", resourceType);
        return exception;
    }
    
    /**
     * 권한 부족 예외 생성 (편의 메서드)
     * 
     * @param action 수행하려는 작업
     * @param resourceType 리소스 타입
     * @param resourceId 리소스 ID
     * @return BusinessException 인스턴스
     */
    public static BusinessException forbidden(String action, String resourceType, Object resourceId) {
        BusinessException exception = new BusinessException(ErrorCode.FORBIDDEN, 
                                   String.format("%s에 대한 권한이 없습니다: %s %s", action, resourceType, resourceId));
        exception.addParameter("action", action)
                .addParameter("resourceType", resourceType)
                .addParameter("resourceId", resourceId);
        return exception;
    }
    
    /**
     * 서버 내부 오류 예외 생성 (편의 메서드)
     * 
     * @param operation 수행하려는 작업
     * @param reason 오류 이유
     * @return BusinessException 인스턴스
     */
    public static BusinessException internalError(String operation, String reason) {
        BusinessException exception = new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, 
                                   String.format("%s 중 내부 오류가 발생했습니다: %s", operation, reason));
        exception.addParameter("operation", operation)
                .addParameter("reason", reason);
        return exception;
    }
}
