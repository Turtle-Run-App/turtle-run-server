package com.turtleRun.be.common.exception;

/**
 * 요청한 리소스를 찾을 수 없을 때 발생하는 예외
 * 모든 도메인에서 공통적으로 사용할 수 있습니다.
 */
public class ResourceNotFoundException extends TurtleRunException {
    
    /**
     * 기본 생성자
     * 
     * @param resourceType 리소스 타입 (예: "사용자", "러닝 세션", "캐릭터")
     * @param resourceId 리소스 ID
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(ErrorCode.RESOURCE_NOT_FOUND, 
              String.format("%s을(를) 찾을 수 없습니다: %s", resourceType, resourceId));
        
        // 파라미터 추가
        addParameter("resourceType", resourceType)
            .addParameter("resourceId", resourceId);
    }
    
    /**
     * 리소스 타입과 필드명을 포함한 생성자
     * 
     * @param resourceType 리소스 타입
     * @param fieldName 필드명
     * @param fieldValue 필드값
     */
    public ResourceNotFoundException(String resourceType, String fieldName, Object fieldValue) {
        super(ErrorCode.RESOURCE_NOT_FOUND, 
              String.format("%s을(를) 찾을 수 없습니다: %s = %s", resourceType, fieldName, fieldValue));
        
        // 파라미터 추가
        addParameter("resourceType", resourceType)
            .addParameter("fieldName", fieldName)
            .addParameter("fieldValue", fieldValue);
    }
    
    /**
     * 사용자 정의 메시지를 포함한 생성자
     * 
     * @param message 사용자 정의 메시지
     */
    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
    
    /**
     * 원인 예외를 포함한 생성자
     * 
     * @param resourceType 리소스 타입
     * @param resourceId 리소스 ID
     * @param cause 원인 예외
     */
    public ResourceNotFoundException(String resourceType, Object resourceId, Throwable cause) {
        super(ErrorCode.RESOURCE_NOT_FOUND, 
              String.format("%s을(를) 찾을 수 없습니다: %s", resourceType, resourceId), 
              cause);
        
        // 파라미터 추가
        addParameter("resourceType", resourceType)
            .addParameter("resourceId", resourceId);
    }
}

