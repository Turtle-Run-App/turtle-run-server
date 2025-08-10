package com.turtleRun.be.running.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 경로 포인트 데이터 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutePointData {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    private LocalDateTime timestamp;
    private BigDecimal speed;
    private BigDecimal verticalAccuracy;
    private BigDecimal horizontalAccuracy;
    private BigDecimal course;
} 