package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HealthKit 동기화 관련 Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthKitSyncResponseDto {
    
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
     * 동기화 상태 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncStatusData {
        
        /**
         * 사용자 ID
         */
        private Long userId;
        
        /**
         * 동기화 상태 (SYNCING, COMPLETED, FAILED, PENDING)
         */
        private String syncStatus;
        
        /**
         * 마지막 동기화 시간
         */
        private LocalDateTime lastSyncTime;
        
        /**
         * 동기화 진행률 (%)
         */
        private Integer progressPercentage;
        
        /**
         * 동기화된 데이터 개수
         */
        private Integer syncedDataCount;
        
        /**
         * 총 데이터 개수
         */
        private Integer totalDataCount;
        
        /**
         * 오류 메시지 (있는 경우)
         */
        private String errorMessage;
    }
    
    /**
     * 동기화 히스토리 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncHistoryData {
        
        /**
         * 사용자 ID
         */
        private Long userId;
        
        /**
         * 동기화 히스토리 목록
         */
        private List<SyncHistoryItem> history;
        
        /**
         * 총 히스토리 개수
         */
        private Integer totalCount;
    }
    
    /**
     * 동기화 히스토리 아이템
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncHistoryItem {
        
        /**
         * 동기화 ID
         */
        private String syncId;
        
        /**
         * 동기화 시작 시간
         */
        private LocalDateTime startTime;
        
        /**
         * 동기화 완료 시간
         */
        private LocalDateTime endTime;
        
        /**
         * 동기화 상태
         */
        private String status;
        
        /**
         * 동기화된 데이터 개수
         */
        private Integer syncedCount;
        
        /**
         * 오류 메시지 (있는 경우)
         */
        private String errorMessage;
    }
}
