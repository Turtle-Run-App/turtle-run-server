package com.turtleRun.be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * TurtleRun 애플리케이션의 모든 에러 코드를 정의하는 열거형
 * 도메인별로 체계적으로 분류되어 있습니다.
 */
@Getter
public enum ErrorCode {
    
    // ===== 공통 에러 코드 (E001~E099) =====
    INVALID_INPUT("E001", "잘못된 입력값입니다", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("E002", "요청한 리소스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("E003", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("E004", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("E005", "서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED("E006", "데이터 검증에 실패했습니다", HttpStatus.BAD_REQUEST),
    DUPLICATE_RESOURCE("E007", "중복된 리소스입니다", HttpStatus.CONFLICT),
    RESOURCE_IN_USE("E008", "사용 중인 리소스입니다", HttpStatus.CONFLICT),
    
    // ===== Running Domain 에러 코드 (R001~R099) =====
    RUNNING_SESSION_NOT_FOUND("R001", "러닝 세션을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    RUNNING_SESSION_ALREADY_COMPLETED("R002", "이미 완료된 러닝 세션입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SESSION_INVALID_DATA("R003", "잘못된 러닝 세션 데이터입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SESSION_IN_PROGRESS("R004", "진행 중인 러닝 세션입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SESSION_DURATION_INVALID("R005", "잘못된 러닝 세션 시간입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SESSION_DISTANCE_INVALID("R006", "잘못된 러닝 세션 거리입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SESSION_CALORIES_INVALID("R007", "잘못된 러닝 세션 칼로리입니다", HttpStatus.BAD_REQUEST),
    RUNNING_HEART_RATE_INVALID("R008", "잘못된 심박수 데이터입니다", HttpStatus.BAD_REQUEST),
    RUNNING_ROUTE_POINT_INVALID("R009", "잘못된 경로 데이터입니다", HttpStatus.BAD_REQUEST),
    RUNNING_SPLIT_INVALID("R010", "잘못된 구간 데이터입니다", HttpStatus.BAD_REQUEST),
    
    // ===== HealthKit Domain 에러 코드 (H001~H099) =====
    HEALTHKIT_SYNC_FAILED("H001", "HealthKit 동기화에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    HEALTHKIT_INVALID_DATA("H002", "잘못된 HealthKit 데이터입니다", HttpStatus.BAD_REQUEST),
    HEALTHKIT_VALIDATION_FAILED("H003", "HealthKit 데이터 검증에 실패했습니다", HttpStatus.BAD_REQUEST),
    HEALTHKIT_SYNC_TIMEOUT("H004", "HealthKit 동기화 시간이 초과되었습니다", HttpStatus.REQUEST_TIMEOUT),
    HEALTHKIT_DATA_NOT_AVAILABLE("H005", "HealthKit 데이터를 사용할 수 없습니다", HttpStatus.SERVICE_UNAVAILABLE),
    HEALTHKIT_PERMISSION_DENIED("H006", "HealthKit 접근 권한이 거부되었습니다", HttpStatus.FORBIDDEN),
    HEALTHKIT_SYNC_IN_PROGRESS("H007", "HealthKit 동기화가 이미 진행 중입니다", HttpStatus.CONFLICT),
    HEALTHKIT_INVALID_SYNC_PERIOD("H008", "잘못된 HealthKit 동기화 기간입니다", HttpStatus.BAD_REQUEST),
    HEALTHKIT_DATA_CORRUPTED("H009", "HealthKit 데이터가 손상되었습니다", HttpStatus.BAD_REQUEST),
    HEALTHKIT_UNSUPPORTED_DATA_TYPE("H010", "지원하지 않는 HealthKit 데이터 타입입니다", HttpStatus.BAD_REQUEST),
    
    // ===== Player Domain 에러 코드 (P001~P099) =====
    PLAYER_USER_NOT_FOUND("P001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PLAYER_INVALID_USER_DATA("P002", "잘못된 사용자 데이터입니다", HttpStatus.BAD_REQUEST),
    PLAYER_CHARACTER_NOT_FOUND("P003", "캐릭터를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PLAYER_INVALID_CHARACTER_DATA("P004", "잘못된 캐릭터 데이터입니다", HttpStatus.BAD_REQUEST),
    PLAYER_LEVEL_INVALID("P005", "잘못된 사용자 레벨입니다", HttpStatus.BAD_REQUEST),
    PLAYER_EXPERIENCE_INVALID("P006", "잘못된 사용자 경험치입니다", HttpStatus.BAD_REQUEST),
    PLAYER_COINS_INVALID("P007", "잘못된 사용자 코인입니다", HttpStatus.BAD_REQUEST),
    PLAYER_ACHIEVEMENT_NOT_FOUND("P008", "업적을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PLAYER_ITEM_NOT_FOUND("P009", "아이템을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PLAYER_INSUFFICIENT_RESOURCES("P010", "리소스가 부족합니다", HttpStatus.BAD_REQUEST),
    
    // ===== Bulk Initial Domain 에러 코드 (B001~B099) =====
    BULK_INITIAL_SYNC_LIMIT_EXCEEDED("B001", "동기화 한도를 초과했습니다", HttpStatus.BAD_REQUEST),
    BULK_INITIAL_SYNC_IN_PROGRESS("B002", "이미 진행 중인 동기화입니다", HttpStatus.CONFLICT),
    BULK_INITIAL_INVALID_SYNC_PERIOD("B003", "잘못된 동기화 기간입니다", HttpStatus.BAD_REQUEST),
    BULK_INITIAL_SYNC_FAILED("B004", "대량 동기화에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    BULK_INITIAL_DATA_TOO_LARGE("B005", "동기화할 데이터가 너무 큽니다", HttpStatus.PAYLOAD_TOO_LARGE),
    BULK_INITIAL_INVALID_BATCH_SIZE("B006", "잘못된 배치 크기입니다", HttpStatus.BAD_REQUEST),
    BULK_INITIAL_SYNC_TIMEOUT("B007", "대량 동기화 시간이 초과되었습니다", HttpStatus.REQUEST_TIMEOUT),
    BULK_INITIAL_PARTIAL_SUCCESS("B008", "일부 데이터만 동기화되었습니다", HttpStatus.PARTIAL_CONTENT),
    BULK_INITIAL_DUPLICATE_DATA("B009", "중복된 데이터가 포함되어 있습니다", HttpStatus.CONFLICT),
    BULK_INITIAL_INVALID_USER_STATUS("B010", "사용자 상태가 동기화에 적합하지 않습니다", HttpStatus.BAD_REQUEST),
    
    // ===== Auth Domain 에러 코드 (A001~A099) =====
    AUTH_USER_NOT_FOUND("A001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    AUTH_INVALID_CREDENTIALS("A002", "잘못된 인증 정보입니다", HttpStatus.UNAUTHORIZED),
    AUTH_EMAIL_ALREADY_EXISTS("A003", "이미 존재하는 이메일입니다", HttpStatus.CONFLICT),
    AUTH_USERNAME_ALREADY_EXISTS("A004", "이미 존재하는 사용자명입니다", HttpStatus.CONFLICT),
    AUTH_INVALID_TOKEN("A005", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("A006", "토큰이 만료되었습니다", HttpStatus.UNAUTHORIZED),
    AUTH_USER_INACTIVE("A007", "비활성화된 사용자입니다", HttpStatus.FORBIDDEN),
    AUTH_EMAIL_NOT_VERIFIED("A008", "이메일이 인증되지 않았습니다", HttpStatus.FORBIDDEN),
    AUTH_INVALID_PASSWORD("A009", "잘못된 비밀번호 형식입니다", HttpStatus.BAD_REQUEST),
    AUTH_ACCOUNT_LOCKED("A010", "계정이 잠겨있습니다", HttpStatus.FORBIDDEN);
    
    /**
     * 에러 코드
     */
    private final String code;
    
    /**
     * 기본 에러 메시지
     */
    private final String defaultMessage;
    
    /**
     * HTTP 상태 코드
     */
    private final HttpStatus httpStatus;
    
    /**
     * 생성자
     * 
     * @param code 에러 코드
     * @param defaultMessage 기본 에러 메시지
     * @param httpStatus HTTP 상태 코드
     */
    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }
    
    /**
     * HTTP 상태 코드 반환
     * 
     * @return HTTP 상태 코드
     */
    public int getHttpStatus() {
        return httpStatus.value();
    }
    
    /**
     * 코드로 ErrorCode 찾기
     * 
     * @param code 찾을 에러 코드
     * @return 해당하는 ErrorCode 또는 null
     */
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}

