package com.turtleRun.be.running.domain.repository;

import com.turtleRun.be.running.domain.entity.RunningHeartRate;
import com.turtleRun.be.running.domain.entity.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunningHeartRateRepository extends JpaRepository<RunningHeartRate, Long> {
    
    /**
     * 세션의 모든 심박수 데이터 조회
     */
    List<RunningHeartRate> findBySessionOrderByTimestampAsc(RunningSession session);
    
    /**
     * 세션의 특정 기간 심박수 데이터 조회
     */
    @Query("SELECT rhr FROM RunningHeartRate rhr WHERE rhr.session = :session " +
           "AND rhr.timestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY rhr.timestamp ASC")
    List<RunningHeartRate> findBySessionAndTimeRange(
            @Param("session") RunningSession session,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 세션의 최고 심박수 데이터 조회
     */
    @Query("SELECT rhr FROM RunningHeartRate rhr WHERE rhr.session = :session " +
           "AND rhr.heartRate = (SELECT MAX(rhr2.heartRate) FROM RunningHeartRate rhr2 WHERE rhr2.session = :session)")
    Optional<RunningHeartRate> findMaxHeartRateBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최저 심박수 데이터 조회
     */
    @Query("SELECT rhr FROM RunningHeartRate rhr WHERE rhr.session = :session " +
           "AND rhr.heartRate = (SELECT MIN(rhr2.heartRate) FROM RunningHeartRate rhr2 WHERE rhr2.session = :session)")
    Optional<RunningHeartRate> findMinHeartRateBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 평균 심박수 조회
     */
    @Query("SELECT AVG(rhr.heartRate) FROM RunningHeartRate rhr WHERE rhr.session = :session")
    Double getAverageHeartRateBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 심박수 데이터 개수 조회
     */
    long countBySession(RunningSession session);
    
    /**
     * 특정 심박수 범위의 데이터 조회
     */
    @Query("SELECT rhr FROM RunningHeartRate rhr WHERE rhr.session = :session " +
           "AND rhr.heartRate BETWEEN :minHeartRate AND :maxHeartRate " +
           "ORDER BY rhr.timestamp ASC")
    List<RunningHeartRate> findBySessionAndHeartRateRange(
            @Param("session") RunningSession session,
            @Param("minHeartRate") Integer minHeartRate,
            @Param("maxHeartRate") Integer maxHeartRate
    );
    
    /**
     * 세션의 특정 시간 이후 심박수 데이터 조회
     */
    @Query("SELECT rhr FROM RunningHeartRate rhr WHERE rhr.session = :session " +
           "AND rhr.timestamp >= :fromTime " +
           "ORDER BY rhr.timestamp ASC")
    List<RunningHeartRate> findBySessionAndFromTime(
            @Param("session") RunningSession session,
            @Param("fromTime") LocalDateTime fromTime
    );
} 