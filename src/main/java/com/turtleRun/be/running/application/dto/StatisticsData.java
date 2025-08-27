package com.turtleRun.be.running.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 러닝 통계 데이터 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsData {
    private BigDecimal averageStrideLength;
    private BigDecimal averageGroundContactTime;
    private BigDecimal averageVerticalOscillation;
    private BigDecimal averagePower;
    private Integer averageCadence;
    private Integer maxHeartRate;
    private Integer averageHeartRate;
    private BigDecimal trainingEffect;
    private BigDecimal vo2max;
} 