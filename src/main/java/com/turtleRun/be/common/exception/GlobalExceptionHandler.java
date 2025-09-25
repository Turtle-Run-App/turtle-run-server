package com.turtleRun.be.common.exception;

import com.turtleRun.be.auth.exception.AuthException;
import com.turtleRun.be.common.model.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리기
 * 애플리케이션에서 발생하는 모든 예외를 일관되게 처리합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===== TurtleRun Custom Exception 처리 =====
    
    /**
     * TurtleRunException 계열의 모든 커스텀 예외를 처리
     */
    @ExceptionHandler(TurtleRunException.class)
    public ResponseEntity<ErrorResponseDto> handleTurtleRunException(TurtleRunException e) {
        log.error("TurtleRun Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(e.getErrorCode().getCode())
                .message(e.getMessage())
                .details(e.getParameters())
                .build();
        
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorResponse);
    }
    
    /**
     * AuthException 처리
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthException(AuthException e) {
        log.error("Auth Exception 발생: {}", e.getMessage(), e);
        
        ErrorCode errorCode = determineAuthErrorCode(e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode.getCode())
                .message(e.getMessage())
                .build();
        
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
    
    /**
     * AuthException의 타입에 따라 적절한 ErrorCode를 결정
     */
    private ErrorCode determineAuthErrorCode(AuthException e) {
        if (e instanceof AuthException.UserNotFound) {
            return ErrorCode.AUTH_USER_NOT_FOUND;
        } else if (e instanceof AuthException.InvalidCredentials) {
            return ErrorCode.AUTH_INVALID_CREDENTIALS;
        } else if (e instanceof AuthException.EmailAlreadyExists) {
            return ErrorCode.AUTH_EMAIL_ALREADY_EXISTS;
        } else if (e instanceof AuthException.UsernameAlreadyExists) {
            return ErrorCode.AUTH_USERNAME_ALREADY_EXISTS;
        } else if (e instanceof AuthException.InvalidToken) {
            return ErrorCode.AUTH_INVALID_TOKEN;
        } else if (e instanceof AuthException.TokenExpired) {
            return ErrorCode.AUTH_TOKEN_EXPIRED;
        } else if (e instanceof AuthException.UserInactive) {
            return ErrorCode.AUTH_USER_INACTIVE;
        } else if (e instanceof AuthException.EmailNotVerified) {
            return ErrorCode.AUTH_EMAIL_NOT_VERIFIED;
        } else {
            return ErrorCode.INTERNAL_SERVER_ERROR;
        }
    }
    
    // ===== Spring Validation Exception 처리 =====
    
    /**
     * @Valid 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation Exception 발생: {}", e.getMessage(), e);
        
        Map<String, Object> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing + "; " + replacement
                ));
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message("입력 데이터 검증에 실패했습니다")
                .details(fieldErrors)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * @Validated 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint Violation Exception 발생: {}", e.getMessage(), e);
        
        Map<String, Object> fieldErrors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing + "; " + replacement
                ));
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message("입력 데이터 검증에 실패했습니다")
                .details(fieldErrors)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * BindException 처리 (폼 데이터 바인딩 실패)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException e) {
        log.error("Bind Exception 발생: {}", e.getMessage(), e);
        
        Map<String, Object> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing + "; " + replacement
                ));
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message("데이터 바인딩에 실패했습니다")
                .details(fieldErrors)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    // ===== Spring Framework Exception 처리 =====
    
    /**
     * HTTP 메시지 읽기 실패 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HTTP Message Not Readable Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("요청 본문을 읽을 수 없습니다")
                .details(Map.of("reason", e.getMessage()))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * 지원하지 않는 HTTP 메서드 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HTTP Request Method Not Supported Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("지원하지 않는 HTTP 메서드입니다")
                .details(Map.of(
                        "method", e.getMethod(),
                        "supportedMethods", e.getSupportedMethods()
                ))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(errorResponse);
    }
    
    /**
     * 핸들러를 찾을 수 없는 예외 처리 (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("No Handler Found Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.RESOURCE_NOT_FOUND.getCode())
                .message("요청한 리소스를 찾을 수 없습니다")
                .details(Map.of(
                        "method", e.getHttpMethod(),
                        "url", e.getRequestURL()
                ))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
    
    /**
     * 필수 요청 파라미터 누락 예외 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("Missing Servlet Request Parameter Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("필수 요청 파라미터가 누락되었습니다")
                .details(Map.of(
                        "parameterName", e.getParameterName(),
                        "parameterType", e.getParameterType()
                ))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * 메서드 인자 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Method Argument Type Mismatch Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("잘못된 파라미터 타입입니다")
                .details(Map.of(
                        "parameterName", e.getName(),
                        "parameterValue", e.getValue(),
                        "requiredType", e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown"
                ))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    // ===== 일반 Exception 처리 =====
    
    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal Argument Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("잘못된 인자가 전달되었습니다")
                .details(Map.of("reason", e.getMessage()))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * IllegalStateException 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal State Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.RESOURCE_IN_USE.getCode())
                .message("리소스가 사용 중입니다")
                .details(Map.of("reason", e.getMessage()))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }
    
    // ===== 기타 예외 처리 =====
    
    /**
     * NumberFormatException 처리
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponseDto> handleNumberFormatException(NumberFormatException e) {
        log.error("Number Format Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message("잘못된 숫자 형식입니다")
                .details(Map.of("reason", e.getMessage()))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    
    /**
     * NullPointerException 처리
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDto> handleNullPointerException(NullPointerException e) {
        log.error("Null Pointer Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("서버 내부 오류가 발생했습니다")
                .details(Map.of("reason", "Null pointer exception"))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    
    // ===== 최종 예외 처리 (catch-all) =====
    
    /**
     * 처리되지 않은 모든 예외를 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        log.error("Unhandled Exception 발생: {}", e.getMessage(), e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("서버 내부 오류가 발생했습니다")
                .details(Map.of("exceptionType", e.getClass().getSimpleName()))
                .build();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
