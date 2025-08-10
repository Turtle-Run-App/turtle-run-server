package com.turtleRun.be.running.application.service;

import com.turtleRun.be.running.application.dto.EndRunningSessionRequest;
import com.turtleRun.be.running.application.dto.HeartRateData;
import com.turtleRun.be.running.application.dto.RoutePointData;
import com.turtleRun.be.running.application.dto.SplitData;
import com.turtleRun.be.running.application.dto.StatisticsData;
import com.turtleRun.be.running.domain.entity.RunningHeartRate;
import com.turtleRun.be.running.domain.entity.RunningRoutePoint;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.entity.RunningSplit;
import com.turtleRun.be.running.domain.repository.RunningSessionRepository;
import com.turtleRun.be.running.domain.service.RunningHeartRateDomainService;
import com.turtleRun.be.running.domain.service.RunningRoutePointDomainService;
import com.turtleRun.be.running.domain.service.RunningSessionDomainService;
import com.turtleRun.be.running.domain.service.RunningSplitDomainService;
import com.turtleRun.be.player.domain.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 러닝 세션 종료를 처리하는 애플리케이션 서비스
 * iOS 클라이언트로부터 받은 러닝 완료 데이터를 처리하고 저장합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EndRunningSessionService {

    private final RunningSessionRepository runningSessionRepository;
    private final RunningSessionDomainService runningSessionDomainService;
    private final RunningHeartRateDomainService heartRateDomainService;
    private final RunningRoutePointDomainService routePointDomainService;
    private final RunningSplitDomainService splitDomainService;

    /**
     * 러닝 세션을 완료하고 관련 데이터를 저장합니다.
     *
     * @param request 러닝 세션 완료 요청 데이터
     * @return 완료된 러닝 세션
     */
    public RunningSession endRunningSession(EndRunningSessionRequest request) {
        log.info("러닝 세션 완료 요청: sessionId={}, playerId={}", request.getSessionId(), request.getPlayerId());

        // 1. 기존 세션 조회 및 검증
        RunningSession session = runningSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Running session not found: " + request.getSessionId()));

        // 2. 세션 완료 처리
        RunningSession completedSession = runningSessionDomainService.completeRunningSession(
                session.getId(),
                request.getEndTime(),
                request.getDuration(),
                request.getActiveDuration(),
                request.getDistance(),
                request.getTotalCalories(),
                request.getAveragePace(),
                request.getBestPace(),
                request.getAverageSpeed(),
                request.getMaxSpeed(),
                request.getTotalAscent(),
                request.getTotalDescent(),
                request.getWeatherTemperature(),
                request.getWeatherHumidity(),
                request.getSourceDevice(),
                request.getSourceApp()
        );

        // 3. 심박수 데이터 저장
        if (request.getHeartRateData() != null && !request.getHeartRateData().isEmpty()) {
            saveHeartRateData(completedSession, request.getHeartRateData());
        }

        // 4. 경로 포인트 데이터 저장
        if (request.getRoutePoints() != null && !request.getRoutePoints().isEmpty()) {
            saveRoutePointData(completedSession, request.getRoutePoints());
        }

        // 5. 스플릿 데이터 저장
        if (request.getSplits() != null && !request.getSplits().isEmpty()) {
            saveSplitData(completedSession, request.getSplits());
        }

        // 6. 세션 통계 생성 및 저장
        if (request.getStatistics() != null) {
            saveSessionStatistics(completedSession, request.getStatistics());
        }

        log.info("러닝 세션 완료 완료: sessionId={}, distance={}m, duration={}s", 
                completedSession.getId(), completedSession.getDistance(), completedSession.getDuration());

        return completedSession;
    }

    /**
     * 심박수 데이터를 저장합니다.
     */
    private void saveHeartRateData(RunningSession session, List<HeartRateData> heartRateDataList) {
        for (HeartRateData data : heartRateDataList) {
            heartRateDomainService.createHeartRate(
                    session,
                    data.getHeartRate(),
                    data.getTimestamp()
            );
        }
        log.debug("심박수 데이터 저장 완료: {}개", heartRateDataList.size());
    }

    /**
     * 경로 포인트 데이터를 저장합니다.
     */
    private void saveRoutePointData(RunningSession session, List<RoutePointData> routePointDataList) {
        for (RoutePointData data : routePointDataList) {
            routePointDomainService.createRoutePoint(
                    session,
                    data.getLatitude(),
                    data.getLongitude(),
                    data.getAltitude(),
                    data.getTimestamp(),
                    data.getSpeed(),
                    data.getVerticalAccuracy(),
                    data.getHorizontalAccuracy(),
                    data.getCourse()
            );
        }
        log.debug("경로 포인트 데이터 저장 완료: {}개", routePointDataList.size());
    }

    /**
     * 스플릿 데이터를 저장합니다.
     */
    private void saveSplitData(RunningSession session, List<SplitData> splitDataList) {
        for (SplitData data : splitDataList) {
            splitDomainService.createSplit(
                    session,
                    data.getSplitDistance(),
                    data.getSplitDuration(),
                    data.getSplitPace(),
                    data.getAverageHeartRate(),
                    data.getStartTime(),
                    data.getEndTime()
            );
        }
        log.debug("스플릿 데이터 저장 완료: {}개", splitDataList.size());
    }

    /**
     * 세션 통계를 저장합니다.
     */
    private void saveSessionStatistics(RunningSession session, StatisticsData statistics) {
        runningSessionDomainService.createRunningSessionStatistics(
                session,
                statistics.getAverageStrideLength(),
                statistics.getAverageGroundContactTime(),
                statistics.getAverageVerticalOscillation(),
                statistics.getAveragePower(),
                statistics.getAverageCadence(),
                statistics.getMaxHeartRate(),
                statistics.getAverageHeartRate(),
                statistics.getTrainingEffect(),
                statistics.getVo2max()
        );
        log.debug("세션 통계 저장 완료");
    }

    /**
     * 러닝 세션 완료 요청 데이터
     */
    public static class EndRunningSessionRequest {
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

        // Getters and Setters
        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

        public Long getPlayerId() { return playerId; }
        public void setPlayerId(Long playerId) { this.playerId = playerId; }

        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }

        public Integer getActiveDuration() { return activeDuration; }
        public void setActiveDuration(Integer activeDuration) { this.activeDuration = activeDuration; }

        public BigDecimal getDistance() { return distance; }
        public void setDistance(BigDecimal distance) { this.distance = distance; }

        public BigDecimal getTotalCalories() { return totalCalories; }
        public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }

        public BigDecimal getAveragePace() { return averagePace; }
        public void setAveragePace(BigDecimal averagePace) { this.averagePace = averagePace; }

        public BigDecimal getBestPace() { return bestPace; }
        public void setBestPace(BigDecimal bestPace) { this.bestPace = bestPace; }

        public BigDecimal getAverageSpeed() { return averageSpeed; }
        public void setAverageSpeed(BigDecimal averageSpeed) { this.averageSpeed = averageSpeed; }

        public BigDecimal getMaxSpeed() { return maxSpeed; }
        public void setMaxSpeed(BigDecimal maxSpeed) { this.maxSpeed = maxSpeed; }

        public BigDecimal getTotalAscent() { return totalAscent; }
        public void setTotalAscent(BigDecimal totalAscent) { this.totalAscent = totalAscent; }

        public BigDecimal getTotalDescent() { return totalDescent; }
        public void setTotalDescent(BigDecimal totalDescent) { this.totalDescent = totalDescent; }

        public BigDecimal getWeatherTemperature() { return weatherTemperature; }
        public void setWeatherTemperature(BigDecimal weatherTemperature) { this.weatherTemperature = weatherTemperature; }

        public Integer getWeatherHumidity() { return weatherHumidity; }
        public void setWeatherHumidity(Integer weatherHumidity) { this.weatherHumidity = weatherHumidity; }

        public String getSourceDevice() { return sourceDevice; }
        public void setSourceDevice(String sourceDevice) { this.sourceDevice = sourceDevice; }

        public String getSourceApp() { return sourceApp; }
        public void setSourceApp(String sourceApp) { this.sourceApp = sourceApp; }

        public List<HeartRateData> getHeartRateData() { return heartRateData; }
        public void setHeartRateData(List<HeartRateData> heartRateData) { this.heartRateData = heartRateData; }

        public List<RoutePointData> getRoutePoints() { return routePoints; }
        public void setRoutePoints(List<RoutePointData> routePoints) { this.routePoints = routePoints; }

        public List<SplitData> getSplits() { return splits; }
        public void setSplits(List<SplitData> splits) { this.splits = splits; }

        public StatisticsData getStatistics() { return statistics; }
        public void setStatistics(StatisticsData statistics) { this.statistics = statistics; }
    }







} 