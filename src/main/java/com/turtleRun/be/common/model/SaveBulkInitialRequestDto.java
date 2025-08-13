package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 신규 사용자의 러닝 데이터 초기 동기화를 위한 Request DTO
 * POST /api/bulkInitial 엔드포인트에서 사용
 * 사용자 가입 시점부터 30일의 러닝 데이터를 대량으로 동기화
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBulkInitialRequestDto {
    
    /**
     * 사용자 ID
     */
    private Long userId;
    
    /**
     * 동기화 시작 시간 (가입 시점)
     */
    private LocalDateTime syncStartTime;
    
    /**
     * 동기화 종료 시간 (현재)
     */
    private LocalDateTime syncEndTime;
    
    /**
     * 러닝 세션 데이터 목록 (가입 시점부터 30일)
     */
    private List<RunningSessionData> runningSessions;
    
    /**
     * 러닝 세션 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunningSessionData {
        
        /**
         * 원본 운동 ID (클라이언트에서 생성한 UUID)
         */
        private String workoutId;
        
        /**
         * 운동 시작 시간
         */
        private LocalDateTime startTime;
        
        /**
         * 운동 종료 시간
         */
        private LocalDateTime endTime;
        
        /**
         * 운동 타입 (running, walking, cycling 등)
         */
        private String workoutType;
        
        /**
         * 총 거리 (미터)
         */
        private BigDecimal distance;
        
        /**
         * 총 운동 시간 (초)
         */
        private Integer duration;
        
        /**
         * 총 칼로리
         */
        private Integer calories;
        
        /**
         * 평균 심박수 (bpm)
         */
        private Integer avgHeartRate;
        
        /**
         * GPS 경로 포인트 목록
         */
        private List<RoutePoint> route;
    }
    
    /**
     * GPS 경로 포인트 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutePoint {
        
        /**
         * 위도
         */
        private BigDecimal latitude;
        
        /**
         * 경도
         */
        private BigDecimal longitude;
        
        /**
         * 타임스탬프
         */
        private LocalDateTime timestamp;
    }
}
