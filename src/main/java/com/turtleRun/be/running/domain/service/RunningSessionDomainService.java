package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.repository.RunningSessionRepository;
import com.turtleRun.be.running.domain.repository.RunningSessionStatisticsRepository;
import com.turtleRun.be.player.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunningSessionDomainService {
    
    private final RunningSessionRepository runningSessionRepository;
    private final RunningSessionStatisticsRepository statisticsRepository;
    
    /**
     * 플레이어의 러닝 세션 생성
     */
    @Transactional
    public RunningSession createRunningSession(Player player, LocalDateTime startTime) {
        RunningSession session = new RunningSession();
        session.setPlayer(player);
        session.setStartTime(startTime);
        session.setCreatedAt(LocalDateTime.now());
        
        return runningSessionRepository.save(session);
    }
    
    /**
     * 러닝 세션 완료
     */
    @Transactional
    public RunningSession completeRunningSession(Long sessionId, LocalDateTime endTime, 
                                              Integer duration, Integer activeDuration,
                                              BigDecimal distance, BigDecimal totalCalories,
                                              BigDecimal averagePace, BigDecimal bestPace,
                                              BigDecimal averageSpeed, BigDecimal maxSpeed,
                                              BigDecimal totalAscent, BigDecimal totalDescent,
                                              BigDecimal weatherTemperature, Integer weatherHumidity,
                                              String sourceDevice, String sourceApp) {
        
        RunningSession session = runningSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Running session not found: " + sessionId));
        
        session.setEndTime(endTime);
        session.setDuration(duration);
        session.setActiveDuration(activeDuration);
        session.setDistance(distance);
        session.setTotalCalories(totalCalories);
        session.setAveragePace(averagePace);
        session.setBestPace(bestPace);
        session.setAverageSpeed(averageSpeed);
        session.setMaxSpeed(maxSpeed);
        session.setTotalAscent(totalAscent);
        session.setTotalDescent(totalDescent);
        session.setWeatherTemperature(weatherTemperature);
        session.setWeatherHumidity(weatherHumidity);
        session.setSourceDevice(sourceDevice);
        session.setSourceApp(sourceApp);
        
        return runningSessionRepository.save(session);
    }
    
    /**
     * 플레이어의 모든 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessions(Player player) {
        return runningSessionRepository.findByPlayerOrderByStartTimeDesc(player);
    }
    
    /**
     * 플레이어의 특정 기간 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByDateRange(Player player, 
                                                                   LocalDateTime startDate, 
                                                                   LocalDateTime endDate) {
        return runningSessionRepository.findByPlayerAndDateRange(player, startDate, endDate);
    }
    
    /**
     * 플레이어의 최근 러닝 세션 조회
     */
    public Optional<RunningSession> getPlayerLatestRunningSession(Player player) {
        return runningSessionRepository.findFirstByPlayerOrderByStartTimeDesc(player);
    }
    
    /**
     * 플레이어의 특정 거리 이상 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByMinDistance(Player player, BigDecimal minDistance) {
        return runningSessionRepository.findByPlayerAndMinDistance(player, minDistance);
    }
    
    /**
     * 플레이어의 특정 지속시간 이상 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByMinDuration(Player player, Integer minDuration) {
        return runningSessionRepository.findByPlayerAndMinDuration(player, minDuration);
    }
    
    /**
     * 플레이어의 최고 페이스 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByBestPace(Player player) {
        return runningSessionRepository.findByPlayerOrderByBestPaceAsc(player);
    }
    
    /**
     * 플레이어의 최장 거리 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByDistance(Player player) {
        return runningSessionRepository.findByPlayerOrderByDistanceDesc(player);
    }
    
    /**
     * 플레이어의 총 러닝 통계 조회
     */
    public PlayerRunningStatistics getPlayerRunningStatistics(Player player) {
        long totalSessions = runningSessionRepository.countByPlayer(player);
        BigDecimal totalDistance = runningSessionRepository.getTotalDistanceByPlayer(player);
        Integer totalDuration = runningSessionRepository.getTotalDurationByPlayer(player);
        
        return new PlayerRunningStatistics(totalSessions, totalDistance, totalDuration);
    }
    
    /**
     * 러닝 세션 통계 생성
     */
    @Transactional
    public RunningSessionStatistics createRunningSessionStatistics(RunningSession session,
                                                                BigDecimal averageStrideLength,
                                                                BigDecimal averageGroundContactTime,
                                                                BigDecimal averageVerticalOscillation,
                                                                BigDecimal averagePower,
                                                                Integer averageCadence,
                                                                Integer maxHeartRate,
                                                                Integer averageHeartRate,
                                                                BigDecimal trainingEffect,
                                                                BigDecimal vo2max) {
        
        RunningSessionStatistics statistics = new RunningSessionStatistics();
        statistics.setSession(session);
        statistics.setAverageStrideLength(averageStrideLength);
        statistics.setAverageGroundContactTime(averageGroundContactTime);
        statistics.setAverageVerticalOscillation(averageVerticalOscillation);
        statistics.setAveragePower(averagePower);
        statistics.setAverageCadence(averageCadence);
        statistics.setMaxHeartRate(maxHeartRate);
        statistics.setAverageHeartRate(averageHeartRate);
        statistics.setTrainingEffect(trainingEffect);
        statistics.setVo2max(vo2max);
        statistics.setCreatedAt(LocalDateTime.now());
        
        return statisticsRepository.save(statistics);
    }
    
    /**
     * 러닝 세션 통계 조회
     */
    public Optional<RunningSessionStatistics> getRunningSessionStatistics(RunningSession session) {
        return statisticsRepository.findBySession(session);
    }
    
    /**
     * 플레이어 러닝 통계 정보
     */
    public static class PlayerRunningStatistics {
        private final long totalSessions;
        private final BigDecimal totalDistance;
        private final Integer totalDuration;
        
        public PlayerRunningStatistics(long totalSessions, BigDecimal totalDistance, Integer totalDuration) {
            this.totalSessions = totalSessions;
            this.totalDistance = totalDistance;
            this.totalDuration = totalDuration;
        }
        
        public long getTotalSessions() { return totalSessions; }
        public BigDecimal getTotalDistance() { return totalDistance; }
        public Integer getTotalDuration() { return totalDuration; }
    }
} 