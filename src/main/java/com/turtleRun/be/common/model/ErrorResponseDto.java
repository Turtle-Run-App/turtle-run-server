package com.turtleRun.be.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 에러 응답을 위한 표준 DTO
 * 모든 예외 상황에서 일관된 에러 응답 구조를 제공합니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    
    /**
     * 요청 성공 여부 (항상 false)
     */
    private Boolean success;
    
    /**
     * 에러 발생 시간
     */
    private LocalDateTime timestamp;
    
    /**
     * 에러 코드 (예: E001, R001, H001 등)
     */
    private String errorCode;
    
    /**
     * 에러 메시지
     */
    private String message;
    
    /**
     * 상세 에러 정보 (필드별 에러, 파라미터 등)
     */
    private Map<String, Object> details;
    
    /**
     * 간단한 에러 응답 생성
     */
    public static ErrorResponseDto simple(String errorCode, String message) {
        return ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .message(message)
                .build();
    }
    
    /**
     * 상세 정보가 포함된 에러 응답 생성
     */
    public static ErrorResponseDto detailed(String errorCode, String message, Map<String, Object> details) {
        return ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .message(message)
                .details(details)
                .build();
    }
}
