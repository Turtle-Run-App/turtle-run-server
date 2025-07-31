package com.turtleRun.be.running.domain.repository;

import com.turtleRun.be.running.domain.entity.RunningRoutePoint;
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
public interface RunningRoutePointRepository extends JpaRepository<RunningRoutePoint, Long> {
    
    /**
     * 세션의 모든 경로 포인트 조회
     */
    List<RunningRoutePoint> findBySessionOrderByTimestampAsc(RunningSession session);
    
    /**
     * 세션의 특정 기간 경로 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.timestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY rrp.timestamp ASC")
    List<RunningRoutePoint> findBySessionAndTimeRange(
            @Param("session") RunningSession session,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 세션의 최고 속도 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.speed = (SELECT MAX(rrp2.speed) FROM RunningRoutePoint rrp2 WHERE rrp2.session = :session)")
    Optional<RunningRoutePoint> findMaxSpeedPointBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최고 고도 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.altitude = (SELECT MAX(rrp2.altitude) FROM RunningRoutePoint rrp2 WHERE rrp2.session = :session)")
    Optional<RunningRoutePoint> findMaxAltitudePointBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 최저 고도 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.altitude = (SELECT MIN(rrp2.altitude) FROM RunningRoutePoint rrp2 WHERE rrp2.session = :session)")
    Optional<RunningRoutePoint> findMinAltitudePointBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 평균 속도 조회
     */
    @Query("SELECT AVG(rrp.speed) FROM RunningRoutePoint rrp WHERE rrp.session = :session")
    Double getAverageSpeedBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 평균 고도 조회
     */
    @Query("SELECT AVG(rrp.altitude) FROM RunningRoutePoint rrp WHERE rrp.session = :session")
    Double getAverageAltitudeBySession(@Param("session") RunningSession session);
    
    /**
     * 세션의 경로 포인트 개수 조회
     */
    long countBySession(RunningSession session);
    
    /**
     * 특정 속도 범위의 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.speed BETWEEN :minSpeed AND :maxSpeed " +
           "ORDER BY rrp.timestamp ASC")
    List<RunningRoutePoint> findBySessionAndSpeedRange(
            @Param("session") RunningSession session,
            @Param("minSpeed") BigDecimal minSpeed,
            @Param("maxSpeed") BigDecimal maxSpeed
    );
    
    /**
     * 특정 고도 범위의 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.altitude BETWEEN :minAltitude AND :maxAltitude " +
           "ORDER BY rrp.timestamp ASC")
    List<RunningRoutePoint> findBySessionAndAltitudeRange(
            @Param("session") RunningSession session,
            @Param("minAltitude") BigDecimal minAltitude,
            @Param("maxAltitude") BigDecimal maxAltitude
    );
    
    /**
     * 특정 좌표 범위의 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "AND rrp.latitude BETWEEN :minLat AND :maxLat " +
           "AND rrp.longitude BETWEEN :minLng AND :maxLng " +
           "ORDER BY rrp.timestamp ASC")
    List<RunningRoutePoint> findBySessionAndCoordinateRange(
            @Param("session") RunningSession session,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLng") BigDecimal minLng,
            @Param("maxLng") BigDecimal maxLng
    );
    
    /**
     * 세션의 시작 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "ORDER BY rrp.timestamp ASC")
    Optional<RunningRoutePoint> findFirstBySessionOrderByTimestampAsc(@Param("session") RunningSession session);
    
    /**
     * 세션의 종료 포인트 조회
     */
    @Query("SELECT rrp FROM RunningRoutePoint rrp WHERE rrp.session = :session " +
           "ORDER BY rrp.timestamp DESC")
    Optional<RunningRoutePoint> findFirstBySessionOrderByTimestampDesc(@Param("session") RunningSession session);
} 