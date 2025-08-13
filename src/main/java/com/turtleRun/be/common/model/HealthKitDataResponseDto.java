package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * HealthKit 데이터 관련 Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthKitDataResponseDto {
    
    /**
     * 응답 성공 여부
     */
    private boolean success;
    
    /**
     * 응답 메시지
     */
    private String message;
    
    /**
     * 응답 시간
     */
    private LocalDateTime timestamp;
    
    /**
     * 응답 데이터 (다양한 타입의 데이터를 담을 수 있음)
     */
    private Object data;
    
    /**
     * 사용자 HealthKit 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserHealthKitData {
        
        /**
         * 사용자 ID
         */
        private Long userId;
        
        /**
         * 데이터 조회 시작 시간
         */
        private LocalDateTime startDate;
        
        /**
         * 데이터 조회 종료 시간
         */
        private LocalDateTime endDate;
        
        /**
         * 러닝 세션 데이터
         */
        private List<RunningSessionData> runningSessions;
        
        /**
         * 심박수 데이터
         */
        private List<HeartRateData> heartRateData;
        
        /**
         * GPS 경로 데이터
         */
        private List<GpsRouteData> gpsRoutes;
        
        /**
         * 총 데이터 개수
         */
        private Integer totalDataCount;
    }
    
    /**
     * 러닝 세션 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunningSessionData {
        
        /**
         * 세션 ID
         */
        private String sessionId;
        
        /**
         * 시작 시간
         */
        private LocalDateTime startTime;
        
        /**
         * 종료 시간
         */
        private LocalDateTime endTime;
        
        /**
         * 거리 (미터)
         */
        private BigDecimal distance;
        
        /**
         * 운동 시간 (초)
         */
        private Integer duration;
        
        /**
         * 칼로리
         */
        private Integer calories;
        
        /**
         * 평균 심박수
         */
        private Integer avgHeartRate;
    }
    
    /**
     * 심박수 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartRateData {
        
        /**
         * 심박수 (bpm)
         */
        private Integer heartRate;
        
        /**
         * 측정 시간
         */
        private LocalDateTime timestamp;
        
        /**
         * 심박수 구간
         */
        private String zone;
    }
    
    /**
     * GPS 경로 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GpsRouteData {
        
        /**
         * 위도
         */
        private BigDecimal latitude;
        
        /**
         * 경도
         */
        private BigDecimal longitude;
        
        /**
         * 고도
         */
        private BigDecimal altitude;
        
        /**
         * 측정 시간
         */
        private LocalDateTime timestamp;
        
        /**
         * 속도 (m/s)
         */
        private BigDecimal speed;
    }
    
    /**
     * HealthKit 통계 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthKitStatsData {
        
        /**
         * 사용자 ID
         */
        private Long userId;
        
        /**
         * 통계 기간
         */
        private String period;
        
        /**
         * 총 러닝 세션 수
         */
        private Integer totalRunningSessions;
        
        /**
         * 총 운동 시간 (초)
         */
        private Integer totalDuration;
        
        /**
         * 총 거리 (미터)
         */
        private BigDecimal totalDistance;
        
        /**
         * 총 칼로리
         */
        private Integer totalCalories;
        
        /**
         * 평균 심박수
         */
        private Integer averageHeartRate;
        
        /**
         * 최고 심박수
         */
        private Integer maxHeartRate;
        
        /**
         * 최저 심박수
         */
        private Integer minHeartRate;
    }
    
    /**
     * 데이터 검증 결과
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataValidationResult {
        
        /**
         * 검증 성공 여부
         */
        private boolean isValid;
        
        /**
         * 검증된 데이터 개수
         */
        private Integer validatedDataCount;
        
        /**
         * 오류가 있는 데이터 개수
         */
        private Integer errorDataCount;
        
        /**
         * 검증 오류 목록
         */
        private List<ValidationError> errors;
        
        /**
         * 검증 완료 시간
         */
        private LocalDateTime validationTime;
    }
    
    /**
     * 검증 오류
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        
        /**
         * 오류 코드
         */
        private String errorCode;
        
        /**
         * 오류 메시지
         */
        private String errorMessage;
        
        /**
         * 오류가 발생한 데이터 ID
         */
        private String dataId;
        
        /**
         * 오류 발생 시간
         */
        private LocalDateTime errorTime;
    }
}
