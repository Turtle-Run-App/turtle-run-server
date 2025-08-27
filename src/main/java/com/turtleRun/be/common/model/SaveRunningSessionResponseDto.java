package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * RunningSession 저장을 위한 Response DTO
 * POST /api/runningSession 엔드포인트의 응답으로 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveRunningSessionResponseDto {
    
    /**
     * 응답 성공 여부
     */
    private boolean success;
    
    /**
     * 응답 메시지
     */
    private String message;
    
    /**
     * 생성된 RunningSession 정보
     */
    private RunningSessionData data;
    
    /**
     * 응답 시간
     */
    private LocalDateTime timestamp;
    
    /**
     * RunningSession 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunningSessionData {
        
        /**
         * 서버에서 생성된 RunningSession ID
         */
        private Long sessionId;
        
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
         * 총 운동 시간 (초)
         */
        private Integer duration;
        
        /**
         * 총 거리 (미터)
         */
        private BigDecimal distance;
        
        /**
         * 총 칼로리
         */
        private Integer calories;
        
        /**
         * 평균 심박수 (bpm)
         */
        private Integer avgHeartRate;
        
        /**
         * 생성 시간
         */
        private LocalDateTime createdAt;
        
        /**
         * 저장된 데이터 개수 요약
         */
        private DataSummary dataSummary;
        

    }
    
    /**
     * 저장된 데이터 개수 요약
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSummary {
        
        /**
         * 저장된 GPS 경로 포인트 개수
         */
        private Integer routePointCount;
        
        /**
         * 저장된 총 거리 (미터)
         */
        private BigDecimal totalDistance;
        
        /**
         * 저장된 총 운동 시간 (초)
         */
        private Integer totalDuration;
        
        /**
         * 저장된 총 칼로리
         */
        private Integer totalCalories;
    }
    

}
