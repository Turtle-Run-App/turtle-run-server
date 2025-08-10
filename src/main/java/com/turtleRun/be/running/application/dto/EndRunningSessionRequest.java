package com.turtleRun.be.running.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 러닝 세션 완료 요청 DTO
 * iOS 클라이언트로부터 받는 러닝 세션 완료 데이터를 담습니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndRunningSessionRequest {
    private Long sessionId;
    private Long playerId;
    private LocalDateTime endTime;
    private Integer duration;
    private Integer activeDuration;
    private BigDecimal distance;
    private BigDecimal totalCalories;
    private BigDecimal averagePace;
    private BigDecimal bestPace;
    private BigDecimal averageSpeed;
    private BigDecimal maxSpeed;
    private BigDecimal totalAscent;
    private BigDecimal totalDescent;
    private BigDecimal weatherTemperature;
    private Integer weatherHumidity;
    private String sourceDevice;
    private String sourceApp;
    private List<HeartRateData> heartRateData;
    private List<RoutePointData> routePoints;
    private List<SplitData> splits;
    private StatisticsData statistics;
} 