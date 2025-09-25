package com.turtleRun.be.running.domain.repository;

import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.entity.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunningSessionStatisticsRepository extends JpaRepository<RunningSessionStatistics, Long> {
    
    /**
     * 세션의 통계 조회
     */
    Optional<RunningSessionStatistics> findBySession(RunningSession session);
    
    /**
     * 특정 평균 심박수 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageHeartRate >= :minHeartRate")
    List<RunningSessionStatistics> findByMinAverageHeartRate(@Param("minHeartRate") Integer minHeartRate);
    
    /**
     * 특정 최고 심박수 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.maxHeartRate >= :minMaxHeartRate")
    List<RunningSessionStatistics> findByMinMaxHeartRate(@Param("minMaxHeartRate") Integer minMaxHeartRate);
    
    /**
     * 특정 평균 보폭 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageStrideLength >= :minStrideLength")
    List<RunningSessionStatistics> findByMinAverageStrideLength(@Param("minStrideLength") BigDecimal minStrideLength);
    
    /**
     * 특정 평균 케이던스 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageCadence >= :minCadence")
    List<RunningSessionStatistics> findByMinAverageCadence(@Param("minCadence") Integer minCadence);
    
    /**
     * 특정 VO2max 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.vo2max >= :minVo2max")
    List<RunningSessionStatistics> findByMinVo2max(@Param("minVo2max") BigDecimal minVo2max);
    
    /**
     * 특정 훈련 효과 이상의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.trainingEffect >= :minTrainingEffect")
    List<RunningSessionStatistics> findByMinTrainingEffect(@Param("minTrainingEffect") BigDecimal minTrainingEffect);
    
    /**
     * 평균 심박수 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageHeartRate BETWEEN :minHeartRate AND :maxHeartRate")
    List<RunningSessionStatistics> findByAverageHeartRateRange(
            @Param("minHeartRate") Integer minHeartRate,
            @Param("maxHeartRate") Integer maxHeartRate
    );
    
    /**
     * 최고 심박수 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.maxHeartRate BETWEEN :minMaxHeartRate AND :maxMaxHeartRate")
    List<RunningSessionStatistics> findByMaxHeartRateRange(
            @Param("minMaxHeartRate") Integer minMaxHeartRate,
            @Param("maxMaxHeartRate") Integer maxMaxHeartRate
    );
    
    /**
     * 평균 보폭 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageStrideLength BETWEEN :minStrideLength AND :maxStrideLength")
    List<RunningSessionStatistics> findByAverageStrideLengthRange(
            @Param("minStrideLength") BigDecimal minStrideLength,
            @Param("maxStrideLength") BigDecimal maxStrideLength
    );
    
    /**
     * 평균 케이던스 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.averageCadence BETWEEN :minCadence AND :maxCadence")
    List<RunningSessionStatistics> findByAverageCadenceRange(
            @Param("minCadence") Integer minCadence,
            @Param("maxCadence") Integer maxCadence
    );
    
    /**
     * VO2max 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.vo2max BETWEEN :minVo2max AND :maxVo2max")
    List<RunningSessionStatistics> findByVo2maxRange(
            @Param("minVo2max") BigDecimal minVo2max,
            @Param("maxVo2max") BigDecimal maxVo2max
    );
    
    /**
     * 훈련 효과 범위의 통계 조회
     */
    @Query("SELECT rss FROM RunningSessionStatistics rss WHERE rss.trainingEffect BETWEEN :minTrainingEffect AND :maxTrainingEffect")
    List<RunningSessionStatistics> findByTrainingEffectRange(
            @Param("minTrainingEffect") BigDecimal minTrainingEffect,
            @Param("maxTrainingEffect") BigDecimal maxTrainingEffect
    );
    
    /**
     * 전체 평균 심박수 조회
     */
    @Query("SELECT AVG(rss.averageHeartRate) FROM RunningSessionStatistics rss")
    Double getOverallAverageHeartRate();
    
    /**
     * 전체 평균 최고 심박수 조회
     */
    @Query("SELECT AVG(rss.maxHeartRate) FROM RunningSessionStatistics rss")
    Double getOverallAverageMaxHeartRate();
    
    /**
     * 전체 평균 보폭 조회
     */
    @Query("SELECT AVG(rss.averageStrideLength) FROM RunningSessionStatistics rss")
    Double getOverallAverageStrideLength();
    
    /**
     * 전체 평균 케이던스 조회
     */
    @Query("SELECT AVG(rss.averageCadence) FROM RunningSessionStatistics rss")
    Double getOverallAverageCadence();
    
    /**
     * 전체 평균 VO2max 조회
     */
    @Query("SELECT AVG(rss.vo2max) FROM RunningSessionStatistics rss")
    Double getOverallAverageVo2max();
    
    /**
     * 전체 평균 훈련 효과 조회
     */
    @Query("SELECT AVG(rss.trainingEffect) FROM RunningSessionStatistics rss")
    Double getOverallAverageTrainingEffect();
} 