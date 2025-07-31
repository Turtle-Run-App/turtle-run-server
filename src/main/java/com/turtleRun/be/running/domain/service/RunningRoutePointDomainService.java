package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningRoutePoint;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningRoutePointRepository;
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
public class RunningRoutePointDomainService {
    
    private final RunningRoutePointRepository routePointRepository;
    
    /**
     * 경로 포인트 생성
     */
    @Transactional
    public RunningRoutePoint createRoutePoint(RunningSession session, BigDecimal latitude, 
                                           BigDecimal longitude, BigDecimal altitude,
                                           LocalDateTime timestamp, BigDecimal speed,
                                           BigDecimal verticalAccuracy, BigDecimal horizontalAccuracy,
                                           BigDecimal course) {
        RunningRoutePoint routePoint = new RunningRoutePoint();
        routePoint.setSession(session);
        routePoint.setLatitude(latitude);
        routePoint.setLongitude(longitude);
        routePoint.setAltitude(altitude);
        routePoint.setTimestamp(timestamp);
        routePoint.setSpeed(speed);
        routePoint.setVerticalAccuracy(verticalAccuracy);
        routePoint.setHorizontalAccuracy(horizontalAccuracy);
        routePoint.setCourse(course);
        routePoint.setCreatedAt(LocalDateTime.now());
        
        return routePointRepository.save(routePoint);
    }
    
    /**
     * 세션의 모든 경로 포인트 조회
     */
    public List<RunningRoutePoint> getSessionRoutePoints(RunningSession session) {
        return routePointRepository.findBySessionOrderByTimestampAsc(session);
    }
    
    /**
     * 세션의 특정 기간 경로 포인트 조회
     */
    public List<RunningRoutePoint> getSessionRoutePointsByTimeRange(RunningSession session, 
                                                                  LocalDateTime startTime, 
                                                                  LocalDateTime endTime) {
        return routePointRepository.findBySessionAndTimeRange(session, startTime, endTime);
    }
    
    /**
     * 세션의 최고 속도 포인트 조회
     */
    public Optional<RunningRoutePoint> getSessionMaxSpeedPoint(RunningSession session) {
        return routePointRepository.findMaxSpeedPointBySession(session);
    }
    
    /**
     * 세션의 최고 고도 포인트 조회
     */
    public Optional<RunningRoutePoint> getSessionMaxAltitudePoint(RunningSession session) {
        return routePointRepository.findMaxAltitudePointBySession(session);
    }
    
    /**
     * 세션의 최저 고도 포인트 조회
     */
    public Optional<RunningRoutePoint> getSessionMinAltitudePoint(RunningSession session) {
        return routePointRepository.findMinAltitudePointBySession(session);
    }
    
    /**
     * 세션의 평균 속도 조회
     */
    public Double getSessionAverageSpeed(RunningSession session) {
        return routePointRepository.getAverageSpeedBySession(session);
    }
    
    /**
     * 세션의 평균 고도 조회
     */
    public Double getSessionAverageAltitude(RunningSession session) {
        return routePointRepository.getAverageAltitudeBySession(session);
    }
    
    /**
     * 세션의 경로 포인트 개수 조회
     */
    public long getSessionRoutePointCount(RunningSession session) {
        return routePointRepository.countBySession(session);
    }
    
    /**
     * 특정 속도 범위의 포인트 조회
     */
    public List<RunningRoutePoint> getSessionRoutePointsBySpeedRange(RunningSession session, 
                                                                    BigDecimal minSpeed, 
                                                                    BigDecimal maxSpeed) {
        return routePointRepository.findBySessionAndSpeedRange(session, minSpeed, maxSpeed);
    }
    
    /**
     * 특정 고도 범위의 포인트 조회
     */
    public List<RunningRoutePoint> getSessionRoutePointsByAltitudeRange(RunningSession session, 
                                                                       BigDecimal minAltitude, 
                                                                       BigDecimal maxAltitude) {
        return routePointRepository.findBySessionAndAltitudeRange(session, minAltitude, maxAltitude);
    }
    
    /**
     * 특정 좌표 범위의 포인트 조회
     */
    public List<RunningRoutePoint> getSessionRoutePointsByCoordinateRange(RunningSession session, 
                                                                         BigDecimal minLat, BigDecimal maxLat,
                                                                         BigDecimal minLng, BigDecimal maxLng) {
        return routePointRepository.findBySessionAndCoordinateRange(session, minLat, maxLat, minLng, maxLng);
    }
    
    /**
     * 세션의 시작 포인트 조회
     */
    public Optional<RunningRoutePoint> getSessionStartPoint(RunningSession session) {
        return routePointRepository.findFirstBySessionOrderByTimestampAsc(session);
    }
    
    /**
     * 세션의 종료 포인트 조회
     */
    public Optional<RunningRoutePoint> getSessionEndPoint(RunningSession session) {
        return routePointRepository.findFirstBySessionOrderByTimestampDesc(session);
    }
    
    /**
     * 세션의 경로 통계 정보
     */
    public RoutePointStatistics getSessionRoutePointStatistics(RunningSession session) {
        Optional<RunningRoutePoint> maxSpeedPoint = getSessionMaxSpeedPoint(session);
        Optional<RunningRoutePoint> maxAltitudePoint = getSessionMaxAltitudePoint(session);
        Optional<RunningRoutePoint> minAltitudePoint = getSessionMinAltitudePoint(session);
        Double averageSpeed = getSessionAverageSpeed(session);
        Double averageAltitude = getSessionAverageAltitude(session);
        long totalCount = getSessionRoutePointCount(session);
        
        return new RoutePointStatistics(
                maxSpeedPoint.map(RunningRoutePoint::getSpeed).orElse(null),
                maxAltitudePoint.map(RunningRoutePoint::getAltitude).orElse(null),
                minAltitudePoint.map(RunningRoutePoint::getAltitude).orElse(null),
                averageSpeed,
                averageAltitude,
                totalCount
        );
    }
    
    /**
     * 경로 포인트 통계 정보
     */
    public static class RoutePointStatistics {
        private final BigDecimal maxSpeed;
        private final BigDecimal maxAltitude;
        private final BigDecimal minAltitude;
        private final Double averageSpeed;
        private final Double averageAltitude;
        private final long totalCount;
        
        public RoutePointStatistics(BigDecimal maxSpeed, BigDecimal maxAltitude, BigDecimal minAltitude,
                                  Double averageSpeed, Double averageAltitude, long totalCount) {
            this.maxSpeed = maxSpeed;
            this.maxAltitude = maxAltitude;
            this.minAltitude = minAltitude;
            this.averageSpeed = averageSpeed;
            this.averageAltitude = averageAltitude;
            this.totalCount = totalCount;
        }
        
        public BigDecimal getMaxSpeed() { return maxSpeed; }
        public BigDecimal getMaxAltitude() { return maxAltitude; }
        public BigDecimal getMinAltitude() { return minAltitude; }
        public Double getAverageSpeed() { return averageSpeed; }
        public Double getAverageAltitude() { return averageAltitude; }
        public long getTotalCount() { return totalCount; }
    }
} 