package com.turtleRun.be.running.domain.repository;

import com.turtleRun.be.running.domain.entity.RunningSplit;
import com.turtleRun.be.running.domain.entity.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunningSplitRepository extends JpaRepository<RunningSplit, Long> {
    
    /**
     * 세션의 모든 스플릿 조회
     */
    List<RunningSplit> findBySessionOrderByStartTimeAsc(RunningSession session);
    
    /**
     * 세션의 특정 기간 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.startTime BETWEEN :startTime AND :endTime " +
           "ORDER BY rs.startTime ASC")
    List<RunningSplit> findBySessionAndTimeRange(
            @Param("session") RunningSession session,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 세션의 최고 페이스 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.splitPace = (SELECT MIN(rs2.splitPace) FROM RunningSplit rs2 WHERE rs2.session = :session)")
    Optional<RunningSplit> findBestPaceSplitBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최저 페이스 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.splitPace = (SELECT MAX(rs2.splitPace) FROM RunningSplit rs2 WHERE rs2.session = :session)")
    Optional<RunningSplit> findWorstPaceSplitBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최고 심박수 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.averageHeartRate = (SELECT MAX(rs2.averageHeartRate) FROM RunningSplit rs2 WHERE rs2.session = :session)")
    Optional<RunningSplit> findMaxHeartRateSplitBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최저 심박수 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.averageHeartRate = (SELECT MIN(rs2.averageHeartRate) FROM RunningSplit rs2 WHERE rs2.session = :session)")
    Optional<RunningSplit> findMinHeartRateSplitBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 평균 페이스 조회
     */
    @Query("SELECT AVG(rs.splitPace) FROM RunningSplit rs WHERE rs.session = :session")
    Double getAveragePaceBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 평균 심박수 조회
     */
    @Query("SELECT AVG(rs.averageHeartRate) FROM RunningSplit rs WHERE rs.session = :session")
    Double getAverageHeartRateBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 스플릿 개수 조회
     */
    long countBySession(RunningSession session);
    
    /**
     * 특정 거리 이상의 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.splitDistance >= :minDistance " +
           "ORDER BY rs.startTime ASC")
    List<RunningSplit> findBySessionAndMinDistance(
            @Param("session") RunningSession session,
            @Param("minDistance") Integer minDistance
    );
    
    /**
     * 특정 지속시간 이상의 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.splitDuration >= :minDuration " +
           "ORDER BY rs.startTime ASC")
    List<RunningSplit> findBySessionAndMinDuration(
            @Param("session") RunningSession session,
            @Param("minDuration") Integer minDuration
    );
    
    /**
     * 특정 페이스 범위의 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.splitPace BETWEEN :minPace AND :maxPace " +
           "ORDER BY rs.startTime ASC")
    List<RunningSplit> findBySessionAndPaceRange(
            @Param("session") RunningSession session,
            @Param("minPace") BigDecimal minPace,
            @Param("maxPace") BigDecimal maxPace
    );
    
    /**
     * 특정 심박수 범위의 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "AND rs.averageHeartRate BETWEEN :minHeartRate AND :maxHeartRate " +
           "ORDER BY rs.startTime ASC")
    List<RunningSplit> findBySessionAndHeartRateRange(
            @Param("session") RunningSession session,
            @Param("minHeartRate") Integer minHeartRate,
            @Param("maxHeartRate") Integer maxHeartRate
    );
    
    /**
     * 세션의 첫 번째 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "ORDER BY rs.startTime ASC")
    Optional<RunningSplit> findFirstBySessionOrderByStartTimeAsc(@Param("session") RunningSession session);
    
    /**
     * 세션의 마지막 스플릿 조회
     */
    @Query("SELECT rs FROM RunningSplit rs WHERE rs.session = :session " +
           "ORDER BY rs.startTime DESC")
    Optional<RunningSplit> findFirstBySessionOrderByStartTimeDesc(@Param("session") RunningSession session);
} 