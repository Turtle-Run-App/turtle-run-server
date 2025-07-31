package com.turtleRun.be.running.domain.repository;

import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.player.domain.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunningSessionRepository extends JpaRepository<RunningSession, Long> {
    /**
     * 플레이어의 모든 러닝 세션 조회
     */
    List<RunningSession> findByPlayerOrderByStartTimeDesc(Player player);
    
    /**
     * 플레이어의 러닝 세션을 페이징으로 조회
     */
    Page<RunningSession> findByPlayerOrderByStartTimeDesc(Player player, Pageable pageable);
    
    /**
     * 플레이어의 특정 기간 러닝 세션 조회
     */
    @Query("SELECT rs FROM RunningSession rs WHERE rs.player = :player " +
           "AND rs.startTime BETWEEN :startDate AND :endDate " +
           "ORDER BY rs.startTime DESC")
    List<RunningSession> findByPlayerAndDateRange(
            @Param("player") Player player,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * 플레이어의 최근 러닝 세션 조회
     */
    Optional<RunningSession> findFirstByPlayerOrderByStartTimeDesc(Player player);
    
    /**
     * 플레이어의 특정 거리 이상의 러닝 세션 조회
     */
    @Query("SELECT rs FROM RunningSession rs WHERE rs.player = :player " +
           "AND rs.distance >= :minDistance " +
           "ORDER BY rs.startTime DESC")
    List<RunningSession> findByPlayerAndMinDistance(
            @Param("player") Player player,
            @Param("minDistance") java.math.BigDecimal minDistance
    );
    
    /**
     * 플레이어의 특정 지속시간 이상의 러닝 세션 조회
     */
    @Query("SELECT rs FROM RunningSession rs WHERE rs.player = :player " +
           "AND rs.duration >= :minDuration " +
           "ORDER BY rs.startTime DESC")
    List<RunningSession> findByPlayerAndMinDuration(
            @Param("player") Player player,
            @Param("minDuration") Integer minDuration
    );
    
    /**
     * 플레이어의 최고 페이스 러닝 세션 조회
     */
    @Query("SELECT rs FROM RunningSession rs WHERE rs.player = :player " +
           "AND rs.bestPace IS NOT NULL " +
           "ORDER BY rs.bestPace ASC")
    List<RunningSession> findByPlayerOrderByBestPaceAsc(Player player);
    
    /**
     * 플레이어의 최장 거리 러닝 세션 조회
     */
    @Query("SELECT rs FROM RunningSession rs WHERE rs.player = :player " +
           "ORDER BY rs.distance DESC")
    List<RunningSession> findByPlayerOrderByDistanceDesc(Player player);
    
    /**
     * 플레이어의 총 러닝 세션 수 조회
     */
    long countByPlayer(Player player);
    
    /**
     * 플레이어의 총 러닝 거리 조회
     */
    @Query("SELECT COALESCE(SUM(rs.distance), 0) FROM RunningSession rs WHERE rs.player = :player")
    java.math.BigDecimal getTotalDistanceByPlayer(@Param("player") Player player);
    
    /**
     * 플레이어의 총 러닝 시간 조회 (초 단위)
     */
    @Query("SELECT COALESCE(SUM(rs.duration), 0) FROM RunningSession rs WHERE rs.player = :player")
    Integer getTotalDurationByPlayer(@Param("player") Player player);
} 