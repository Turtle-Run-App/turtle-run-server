package com.turtleRun.be.running.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "running_session_statistics")
@Getter
@Setter
@NoArgsConstructor
public class RunningSessionStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private RunningSession session;

    private BigDecimal averageStrideLength;
    private BigDecimal averageGroundContactTime;
    private BigDecimal averageVerticalOscillation;
    private BigDecimal averagePower;
    private Integer averageCadence;
    private Integer maxHeartRate;
    private Integer averageHeartRate;
    private BigDecimal trainingEffect;
    private BigDecimal vo2max;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
