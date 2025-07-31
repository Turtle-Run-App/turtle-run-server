package com.turtleRun.be.running.application.service;

import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.service.RunningSessionDomainService;
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
public class RunningApplicationService {
    
    private final RunningSessionDomainService runningSessionDomainService;
    
    /**
     * 러닝 세션 시작
     */
    @Transactional
    public RunningSession startRunningSession(Player player) {
        return runningSessionDomainService.createRunningSession(player, LocalDateTime.now());
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
        
        return runningSessionDomainService.completeRunningSession(
                sessionId, endTime, duration, activeDuration, distance, totalCalories,
                averagePace, bestPace, averageSpeed, maxSpeed, totalAscent, totalDescent,
                weatherTemperature, weatherHumidity, sourceDevice, sourceApp
        );
    }
    
    /**
     * 플레이어의 러닝 세션 목록 조회
     */
    public List<RunningSession> getPlayerRunningSessions(Player player) {
        return runningSessionDomainService.getPlayerRunningSessions(player);
    }
    
    /**
     * 플레이어의 특정 기간 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByDateRange(Player player, 
                                                                   LocalDateTime startDate, 
                                                                   LocalDateTime endDate) {
        return runningSessionDomainService.getPlayerRunningSessionsByDateRange(player, startDate, endDate);
    }
    
    /**
     * 플레이어의 최근 러닝 세션 조회
     */
    public Optional<RunningSession> getPlayerLatestRunningSession(Player player) {
        return runningSessionDomainService.getPlayerLatestRunningSession(player);
    }
    
    /**
     * 플레이어의 특정 거리 이상 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByMinDistance(Player player, BigDecimal minDistance) {
        return runningSessionDomainService.getPlayerRunningSessionsByMinDistance(player, minDistance);
    }
    
    /**
     * 플레이어의 특정 지속시간 이상 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByMinDuration(Player player, Integer minDuration) {
        return runningSessionDomainService.getPlayerRunningSessionsByMinDuration(player, minDuration);
    }
    
    /**
     * 플레이어의 최고 페이스 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByBestPace(Player player) {
        return runningSessionDomainService.getPlayerRunningSessionsByBestPace(player);
    }
    
    /**
     * 플레이어의 최장 거리 러닝 세션 조회
     */
    public List<RunningSession> getPlayerRunningSessionsByDistance(Player player) {
        return runningSessionDomainService.getPlayerRunningSessionsByDistance(player);
    }
    
    /**
     * 플레이어의 러닝 통계 조회
     */
    public RunningSessionDomainService.PlayerRunningStatistics getPlayerRunningStatistics(Player player) {
        return runningSessionDomainService.getPlayerRunningStatistics(player);
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
        
        return runningSessionDomainService.createRunningSessionStatistics(
                session, averageStrideLength, averageGroundContactTime, averageVerticalOscillation,
                averagePower, averageCadence, maxHeartRate, averageHeartRate, trainingEffect, vo2max
        );
    }
    
    /**
     * 러닝 세션 통계 조회
     */
    public Optional<RunningSessionStatistics> getRunningSessionStatistics(RunningSession session) {
        return runningSessionDomainService.getRunningSessionStatistics(session);
    }
} 