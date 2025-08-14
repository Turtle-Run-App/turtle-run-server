package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HealthKit 동기화 관련 Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthKitSyncRequestDto {
    
    /**
     * 사용자 ID
     */
    private String userId;
    
    /**
     * 동기화 시작 시간
     */
    private LocalDateTime syncStartTime;
    
    /**
     * 동기화 종료 시간
     */
    private LocalDateTime syncEndTime;
    
    /**
     * HealthKit 데이터 목록
     */
    private List<HealthKitData> healthKitDataList;
    
    /**
     * HealthKit 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthKitData {
        
        /**
         * 데이터 타입 (steps, distance, heartRate, calories 등)
         */
        private String dataType;
        
        /**
         * 데이터 값
         */
        private Object value;
        
        /**
         * 데이터 단위
         */
        private String unit;
        
        /**
         * 시작 시간
         */
        private LocalDateTime startTime;
        
        /**
         * 종료 시간
         */
        private LocalDateTime endTime;
        
        /**
         * 데이터 소스
         */
        private String source;
        
        /**
         * 메타데이터
         */
        private String metadata;
    }
}
