package com.turtleRun.be.running.domain;

import com.turtleRun.be.player.domain.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "running_sessions")
@Getter
@Setter
@NoArgsConstructor
public class RunningSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;  // 초 단위
    private Integer activeDuration;  // 실제 움직인 시간
    private BigDecimal distance;  // 미터 단위
    private BigDecimal totalCalories;
    private BigDecimal averagePace;  // 분/km
    private BigDecimal bestPace;
    private BigDecimal averageSpeed;  // m/s
    private BigDecimal maxSpeed;
    private BigDecimal totalAscent;
    private BigDecimal totalDescent;
    private BigDecimal weatherTemperature;
    private Integer weatherHumidity;
    private String sourceDevice;
    private String sourceApp;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<RunningHeartRate> heartRates = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<RunningRoutePoint> routePoints = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<RunningSplit> splits = new ArrayList<>();

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private RunningSessionStatistics statistics;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
