package com.turtleRun.be.common.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 신규 사용자의 러닝 데이터 초기 동기화를 위한 Response DTO
 * POST /api/bulkInitial 엔드포인트의 응답으로 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBulkInitialResponseDto {
    
    /**
     * 응답 성공 여부
     */
    private boolean success;
    
    /**
     * 응답 메시지
     */
    private String message;
    
    /**
     * 동기화 결과 데이터
     */
    private SyncResultData data;
    
    /**
     * 응답 시간
     */
    private LocalDateTime timestamp;
    
    /**
     * 동기화 결과 데이터
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncResultData {
        
        /**
         * 사용자 ID
         */
        private Long userId;
        
        /**
         * 동기화 시작 시간
         */
        private LocalDateTime syncStartTime;
        
        /**
         * 동기화 완료 시간
         */
        private LocalDateTime syncEndTime;
        
        /**
         * 총 동기화 시간 (밀리초)
         */
        private Long totalSyncTimeMs;
        
        /**
         * 동기화된 러닝 세션 수
         */
        private Integer totalSessionsProcessed;
        
        /**
         * 성공적으로 동기화된 세션 수
         */
        private Integer successfulSessionsCount;
        
        /**
         * 실패한 세션 수
         */
        private Integer failedSessionsCount;
        
        /**
         * 동기화 성공률 (%)
         */
        private Double successRate;
        
        /**
         * 동기화된 데이터 요약
         */
        private DataSummary dataSummary;
        
        /**
         * 성공한 세션 목록
         */
        private List<SuccessfulSession> successfulSessions;
        
        /**
         * 실패한 세션 목록
         */
        private List<FailedSession> failedSessions;
    }
    
    /**
     * 성공적으로 동기화된 세션 정보
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessfulSession {
        
        /**
         * 원본 운동 ID (클라이언트에서 생성한 UUID)
         */
        private String workoutId;
        
        /**
         * 서버에서 생성된 세션 ID
         */
        private Long serverSessionId;
        
        /**
         * 동기화 완료 시간
         */
        private LocalDateTime syncedAt;
        
        /**
         * 동기화된 데이터 개수
         */
        private DataSummary dataCount;
    }
    
    /**
     * 실패한 세션 정보
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedSession {
        
        /**
         * 원본 운동 ID (클라이언트에서 생성한 UUID)
         */
        private String workoutId;
        
        /**
         * 실패 원인
         */
        private String failureReason;
        
        /**
         * 실패 시간
         */
        private LocalDateTime failedAt;
        
        /**
         * 에러 상세 메시지
         */
        private String errorMessage;
    }
    
    /**
     * 동기화된 데이터 요약
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSummary {
        
        /**
         * 동기화된 러닝 세션
         */
        private Integer runningSessions;
        
        /**
         * 동기화된 GPS 경로 포인트
         */
        private Integer routePoints;
        
        /**
         * 동기화된 총 거리 (미터)
         */
        private BigDecimal totalDistance;
        
        /**
         * 동기화된 총 운동 시간 (초)
         */
        private Integer totalDuration;
        
        /**
         * 동기화된 총 칼로리
         */
        private Integer totalCalories;
    }
}
