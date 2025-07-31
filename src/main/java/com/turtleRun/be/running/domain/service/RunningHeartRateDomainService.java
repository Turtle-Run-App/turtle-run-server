package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningHeartRate;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningHeartRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunningHeartRateDomainService {
    
    private final RunningHeartRateRepository heartRateRepository;
    
    /**
     * 심박수 데이터 생성
     */
    @Transactional
    public RunningHeartRate createHeartRate(RunningSession session, Integer heartRate, LocalDateTime timestamp) {
        RunningHeartRate heartRateData = new RunningHeartRate();
        heartRateData.setSession(session);
        heartRateData.setHeartRate(heartRate);
        heartRateData.setTimestamp(timestamp);
        heartRateData.setCreatedAt(LocalDateTime.now());
        
        return heartRateRepository.save(heartRateData);
    }
    
    /**
     * 세션의 모든 심박수 데이터 조회
     */
    public List<RunningHeartRate> getSessionHeartRates(RunningSession session) {
        return heartRateRepository.findBySessionOrderByTimestampAsc(session);
    }
    
    /**
     * 세션의 특정 기간 심박수 데이터 조회
     */
    public List<RunningHeartRate> getSessionHeartRatesByTimeRange(RunningSession session, 
                                                                 LocalDateTime startTime, 
                                                                 LocalDateTime endTime) {
        return heartRateRepository.findBySessionAndTimeRange(session, startTime, endTime);
    }
    
    /**
     * 세션의 최고 심박수 데이터 조회
     */
    public Optional<RunningHeartRate> getSessionMaxHeartRate(RunningSession session) {
        return heartRateRepository.findMaxHeartRateBySession(session);
    }
    
    /**
     * 세션의 최저 심박수 데이터 조회
     */
    public Optional<RunningHeartRate> getSessionMinHeartRate(RunningSession session) {
        return heartRateRepository.findMinHeartRateBySession(session);
    }
    
    /**
     * 세션의 평균 심박수 조회
     */
    public Double getSessionAverageHeartRate(RunningSession session) {
        return heartRateRepository.getAverageHeartRateBySession(session);
    }
    
    /**
     * 세션의 심박수 데이터 개수 조회
     */
    public long getSessionHeartRateCount(RunningSession session) {
        return heartRateRepository.countBySession(session);
    }
    
    /**
     * 특정 심박수 범위의 데이터 조회
     */
    public List<RunningHeartRate> getSessionHeartRatesByRange(RunningSession session, 
                                                             Integer minHeartRate, 
                                                             Integer maxHeartRate) {
        return heartRateRepository.findBySessionAndHeartRateRange(session, minHeartRate, maxHeartRate);
    }
    
    /**
     * 세션의 특정 시간 이후 심박수 데이터 조회
     */
    public List<RunningHeartRate> getSessionHeartRatesFromTime(RunningSession session, LocalDateTime fromTime) {
        return heartRateRepository.findBySessionAndFromTime(session, fromTime);
    }
    
    /**
     * 세션의 심박수 통계 정보
     */
    public HeartRateStatistics getSessionHeartRateStatistics(RunningSession session) {
        Optional<RunningHeartRate> maxHeartRate = getSessionMaxHeartRate(session);
        Optional<RunningHeartRate> minHeartRate = getSessionMinHeartRate(session);
        Double averageHeartRate = getSessionAverageHeartRate(session);
        long totalCount = getSessionHeartRateCount(session);
        
        return new HeartRateStatistics(
                maxHeartRate.map(RunningHeartRate::getHeartRate).orElse(null),
                minHeartRate.map(RunningHeartRate::getHeartRate).orElse(null),
                averageHeartRate,
                totalCount
        );
    }
    
    /**
     * 심박수 통계 정보
     */
    public static class HeartRateStatistics {
        private final Integer maxHeartRate;
        private final Integer minHeartRate;
        private final Double averageHeartRate;
        private final long totalCount;
        
        public HeartRateStatistics(Integer maxHeartRate, Integer minHeartRate, 
                                 Double averageHeartRate, long totalCount) {
            this.maxHeartRate = maxHeartRate;
            this.minHeartRate = minHeartRate;
            this.averageHeartRate = averageHeartRate;
            this.totalCount = totalCount;
        }
        
        public Integer getMaxHeartRate() { return maxHeartRate; }
        public Integer getMinHeartRate() { return minHeartRate; }
        public Double getAverageHeartRate() { return averageHeartRate; }
        public long getTotalCount() { return totalCount; }
    }
} 