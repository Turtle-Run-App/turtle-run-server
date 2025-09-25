package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningSplit;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningSplitRepository;
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
public class RunningSplitDomainService {
    
    private final RunningSplitRepository splitRepository;
    
    /**
     * 스플릿 생성
     */
    @Transactional
    public RunningSplit createSplit(RunningSession session, Integer splitDistance, Integer splitDuration,
                                  BigDecimal splitPace, Integer averageHeartRate,
                                  LocalDateTime startTime, LocalDateTime endTime) {
        RunningSplit split = new RunningSplit();
        split.setSession(session);
        split.setSplitDistance(splitDistance);
        split.setSplitDuration(splitDuration);
        split.setSplitPace(splitPace);
        split.setAverageHeartRate(averageHeartRate);
        split.setStartTime(startTime);
        split.setEndTime(endTime);
        split.setCreatedAt(LocalDateTime.now());
        
        return splitRepository.save(split);
    }
    
    /**
     * 세션의 모든 스플릿 조회
     */
    public List<RunningSplit> getSessionSplits(RunningSession session) {
        return splitRepository.findBySessionOrderByStartTimeAsc(session);
    }
    
    /**
     * 세션의 특정 기간 스플릿 조회
     */
    public List<RunningSplit> getSessionSplitsByTimeRange(RunningSession session, 
                                                         LocalDateTime startTime, 
                                                         LocalDateTime endTime) {
        return splitRepository.findBySessionAndTimeRange(session, startTime, endTime);
    }
    
    /**
     * 세션의 최고 페이스 스플릿 조회
     */
    public Optional<RunningSplit> getSessionBestPaceSplit(RunningSession session) {
        return splitRepository.findBestPaceSplitBySession(session);
    }
    
    /**
     * 세션의 최저 페이스 스플릿 조회
     */
    public Optional<RunningSplit> getSessionWorstPaceSplit(RunningSession session) {
        return splitRepository.findWorstPaceSplitBySession(session);
    }
    
    /**
     * 세션의 최고 심박수 스플릿 조회
     */
    public Optional<RunningSplit> getSessionMaxHeartRateSplit(RunningSession session) {
        return splitRepository.findMaxHeartRateSplitBySession(session);
    }
    
    /**
     * 세션의 최저 심박수 스플릿 조회
     */
    public Optional<RunningSplit> getSessionMinHeartRateSplit(RunningSession session) {
        return splitRepository.findMinHeartRateSplitBySession(session);
    }
    
    /**
     * 세션의 평균 페이스 조회
     */
    public Double getSessionAveragePace(RunningSession session) {
        return splitRepository.getAveragePaceBySession(session);
    }
    
    /**
     * 세션의 평균 심박수 조회
     */
    public Double getSessionAverageHeartRate(RunningSession session) {
        return splitRepository.getAverageHeartRateBySession(session);
    }
    
    /**
     * 세션의 스플릿 개수 조회
     */
    public long getSessionSplitCount(RunningSession session) {
        return splitRepository.countBySession(session);
    }
    
    /**
     * 특정 거리 이상의 스플릿 조회
     */
    public List<RunningSplit> getSessionSplitsByMinDistance(RunningSession session, Integer minDistance) {
        return splitRepository.findBySessionAndMinDistance(session, minDistance);
    }
    
    /**
     * 특정 지속시간 이상의 스플릿 조회
     */
    public List<RunningSplit> getSessionSplitsByMinDuration(RunningSession session, Integer minDuration) {
        return splitRepository.findBySessionAndMinDuration(session, minDuration);
    }
    
    /**
     * 특정 페이스 범위의 스플릿 조회
     */
    public List<RunningSplit> getSessionSplitsByPaceRange(RunningSession session, 
                                                         BigDecimal minPace, 
                                                         BigDecimal maxPace) {
        return splitRepository.findBySessionAndPaceRange(session, minPace, maxPace);
    }
    
    /**
     * 특정 심박수 범위의 스플릿 조회
     */
    public List<RunningSplit> getSessionSplitsByHeartRateRange(RunningSession session, 
                                                              Integer minHeartRate, 
                                                              Integer maxHeartRate) {
        return splitRepository.findBySessionAndHeartRateRange(session, minHeartRate, maxHeartRate);
    }
    
    /**
     * 세션의 첫 번째 스플릿 조회
     */
    public Optional<RunningSplit> getSessionFirstSplit(RunningSession session) {
        return splitRepository.findFirstBySessionOrderByStartTimeAsc(session);
    }
    
    /**
     * 세션의 마지막 스플릿 조회
     */
    public Optional<RunningSplit> getSessionLastSplit(RunningSession session) {
        return splitRepository.findFirstBySessionOrderByStartTimeDesc(session);
    }
    
    /**
     * 세션의 스플릿 통계 정보
     */
    public SplitStatistics getSessionSplitStatistics(RunningSession session) {
        Optional<RunningSplit> bestPaceSplit = getSessionBestPaceSplit(session);
        Optional<RunningSplit> worstPaceSplit = getSessionWorstPaceSplit(session);
        Optional<RunningSplit> maxHeartRateSplit = getSessionMaxHeartRateSplit(session);
        Optional<RunningSplit> minHeartRateSplit = getSessionMinHeartRateSplit(session);
        Double averagePace = getSessionAveragePace(session);
        Double averageHeartRate = getSessionAverageHeartRate(session);
        long totalCount = getSessionSplitCount(session);
        
        return new SplitStatistics(
                bestPaceSplit.map(RunningSplit::getSplitPace).orElse(null),
                worstPaceSplit.map(RunningSplit::getSplitPace).orElse(null),
                maxHeartRateSplit.map(RunningSplit::getAverageHeartRate).orElse(null),
                minHeartRateSplit.map(RunningSplit::getAverageHeartRate).orElse(null),
                averagePace,
                averageHeartRate,
                totalCount
        );
    }
    
    /**
     * 스플릿 통계 정보
     */
    public static class SplitStatistics {
        private final BigDecimal bestPace;
        private final BigDecimal worstPace;
        private final Integer maxHeartRate;
        private final Integer minHeartRate;
        private final Double averagePace;
        private final Double averageHeartRate;
        private final long totalCount;
        
        public SplitStatistics(BigDecimal bestPace, BigDecimal worstPace, Integer maxHeartRate, 
                             Integer minHeartRate, Double averagePace, Double averageHeartRate, long totalCount) {
            this.bestPace = bestPace;
            this.worstPace = worstPace;
            this.maxHeartRate = maxHeartRate;
            this.minHeartRate = minHeartRate;
            this.averagePace = averagePace;
            this.averageHeartRate = averageHeartRate;
            this.totalCount = totalCount;
        }
        
        public BigDecimal getBestPace() { return bestPace; }
        public BigDecimal getWorstPace() { return worstPace; }
        public Integer getMaxHeartRate() { return maxHeartRate; }
        public Integer getMinHeartRate() { return minHeartRate; }
        public Double getAveragePace() { return averagePace; }
        public Double getAverageHeartRate() { return averageHeartRate; }
        public long getTotalCount() { return totalCount; }
    }
} 