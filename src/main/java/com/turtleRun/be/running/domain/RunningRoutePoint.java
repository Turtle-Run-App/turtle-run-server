package com.turtleRun.be.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "running_route_points")
@Getter
@Setter
@NoArgsConstructor
public class RunningRoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private RunningSession session;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    private LocalDateTime timestamp;
    private BigDecimal speed;
    private BigDecimal verticalAccuracy;
    private BigDecimal horizontalAccuracy;
    private BigDecimal course;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
