package com.turtleRun.be.running.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 스플릿 데이터 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitData {
    private Integer splitDistance;
    private Integer splitDuration;
    private BigDecimal splitPace;
    private Integer averageHeartRate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
} 