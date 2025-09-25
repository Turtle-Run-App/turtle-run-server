package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningRoutePoint;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningRoutePointRepository;
import com.turtleRun.be.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningRoutePointDomainServiceTest {

    @Mock
    private RunningRoutePointRepository routePointRepository;

    @InjectMocks
    private RunningRoutePointDomainService routePointDomainService;

    private Player testPlayer;
    private RunningSession testSession;
    private RunningRoutePoint testRoutePoint;

    @BeforeEach
    void setUp() {
        testPlayer = Player.builder()
                .playerId(1L)
                .build();

        testSession = new RunningSession();
        testSession.setId(1L);
        testSession.setPlayer(testPlayer);
        testSession.setStartTime(LocalDateTime.now());

        testRoutePoint = new RunningRoutePoint();
        testRoutePoint.setId(1L);
        testRoutePoint.setSession(testSession);
        testRoutePoint.setLatitude(new BigDecimal("37.5665"));
        testRoutePoint.setLongitude(new BigDecimal("126.9780"));
        testRoutePoint.setAltitude(new BigDecimal("50.0"));
        testRoutePoint.setTimestamp(LocalDateTime.now());
        testRoutePoint.setSpeed(new BigDecimal("2.5"));
        testRoutePoint.setVerticalAccuracy(new BigDecimal("5.0"));
        testRoutePoint.setHorizontalAccuracy(new BigDecimal("3.0"));
        testRoutePoint.setCourse(new BigDecimal("90.0"));
    }

    @Test
    @DisplayName("경로 포인트 생성 테스트")
    void createRoutePoint_ShouldCreateRoutePoint() {
        // given
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");
        BigDecimal altitude = new BigDecimal("50.0");
        LocalDateTime timestamp = LocalDateTime.now();
        BigDecimal speed = new BigDecimal("2.5");
        BigDecimal verticalAccuracy = new BigDecimal("5.0");
        BigDecimal horizontalAccuracy = new BigDecimal("3.0");
        BigDecimal course = new BigDecimal("90.0");

        when(routePointRepository.save(any(RunningRoutePoint.class))).thenReturn(testRoutePoint);

        // when
        RunningRoutePoint result = routePointDomainService.createRoutePoint(
                testSession, latitude, longitude, altitude, timestamp, speed,
                verticalAccuracy, horizontalAccuracy, course
        );

        // then
        assertThat(result).isNotNull();
        verify(routePointRepository, times(1)).save(any(RunningRoutePoint.class));
    }

    @Test
    @DisplayName("세션의 모든 경로 포인트 조회 테스트")
    void getSessionRoutePoints_ShouldReturnRoutePoints() {
        // given
        List<RunningRoutePoint> expectedRoutePoints = Arrays.asList(testRoutePoint);
        when(routePointRepository.findBySessionOrderByTimestampAsc(any(RunningSession.class))).thenReturn(expectedRoutePoints);

        // when
        List<RunningRoutePoint> result = routePointDomainService.getSessionRoutePoints(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findBySessionOrderByTimestampAsc(testSession);
    }

    @Test
    @DisplayName("세션의 특정 기간 경로 포인트 조회 테스트")
    void getSessionRoutePointsByTimeRange_ShouldReturnRoutePoints() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        List<RunningRoutePoint> expectedRoutePoints = Arrays.asList(testRoutePoint);
        when(routePointRepository.findBySessionAndTimeRange(any(RunningSession.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedRoutePoints);

        // when
        List<RunningRoutePoint> result = routePointDomainService.getSessionRoutePointsByTimeRange(
                testSession, startTime, endTime);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(routePointRepository, times(1)).findBySessionAndTimeRange(testSession, startTime, endTime);
    }

    @Test
    @DisplayName("세션의 최고 속도 포인트 조회 테스트")
    void getSessionMaxSpeedPoint_ShouldReturnMaxSpeedPoint() {
        // given
        when(routePointRepository.findMaxSpeedPointBySession(any(RunningSession.class))).thenReturn(Optional.of(testRoutePoint));

        // when
        Optional<RunningRoutePoint> result = routePointDomainService.getSessionMaxSpeedPoint(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findMaxSpeedPointBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최고 고도 포인트 조회 테스트")
    void getSessionMaxAltitudePoint_ShouldReturnMaxAltitudePoint() {
        // given
        when(routePointRepository.findMaxAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.of(testRoutePoint));

        // when
        Optional<RunningRoutePoint> result = routePointDomainService.getSessionMaxAltitudePoint(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findMaxAltitudePointBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최저 고도 포인트 조회 테스트")
    void getSessionMinAltitudePoint_ShouldReturnMinAltitudePoint() {
        // given
        when(routePointRepository.findMinAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.of(testRoutePoint));

        // when
        Optional<RunningRoutePoint> result = routePointDomainService.getSessionMinAltitudePoint(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findMinAltitudePointBySession(testSession);
    }

    @Test
    @DisplayName("세션의 평균 속도 조회 테스트")
    void getSessionAverageSpeed_ShouldReturnAverageSpeed() {
        // given
        Double expectedAverageSpeed = 2.8;
        when(routePointRepository.getAverageSpeedBySession(any(RunningSession.class))).thenReturn(expectedAverageSpeed);

        // when
        Double result = routePointDomainService.getSessionAverageSpeed(testSession);

        // then
        assertThat(result).isEqualTo(expectedAverageSpeed);
        verify(routePointRepository, times(1)).getAverageSpeedBySession(testSession);
    }

    @Test
    @DisplayName("세션의 평균 고도 조회 테스트")
    void getSessionAverageAltitude_ShouldReturnAverageAltitude() {
        // given
        Double expectedAverageAltitude = 45.5;
        when(routePointRepository.getAverageAltitudeBySession(any(RunningSession.class))).thenReturn(expectedAverageAltitude);

        // when
        Double result = routePointDomainService.getSessionAverageAltitude(testSession);

        // then
        assertThat(result).isEqualTo(expectedAverageAltitude);
        verify(routePointRepository, times(1)).getAverageAltitudeBySession(testSession);
    }

    @Test
    @DisplayName("세션의 경로 포인트 개수 조회 테스트")
    void getSessionRoutePointCount_ShouldReturnCount() {
        // given
        long expectedCount = 500L;
        when(routePointRepository.countBySession(any(RunningSession.class))).thenReturn(expectedCount);

        // when
        long result = routePointDomainService.getSessionRoutePointCount(testSession);

        // then
        assertThat(result).isEqualTo(expectedCount);
        verify(routePointRepository, times(1)).countBySession(testSession);
    }

    @Test
    @DisplayName("특정 속도 범위의 포인트 조회 테스트")
    void getSessionRoutePointsBySpeedRange_ShouldReturnFilteredRoutePoints() {
        // given
        BigDecimal minSpeed = new BigDecimal("2.0");
        BigDecimal maxSpeed = new BigDecimal("3.0");
        List<RunningRoutePoint> expectedRoutePoints = Arrays.asList(testRoutePoint);
        when(routePointRepository.findBySessionAndSpeedRange(any(RunningSession.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(expectedRoutePoints);

        // when
        List<RunningRoutePoint> result = routePointDomainService.getSessionRoutePointsBySpeedRange(
                testSession, minSpeed, maxSpeed);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(routePointRepository, times(1)).findBySessionAndSpeedRange(testSession, minSpeed, maxSpeed);
    }

    @Test
    @DisplayName("특정 고도 범위의 포인트 조회 테스트")
    void getSessionRoutePointsByAltitudeRange_ShouldReturnFilteredRoutePoints() {
        // given
        BigDecimal minAltitude = new BigDecimal("40.0");
        BigDecimal maxAltitude = new BigDecimal("60.0");
        List<RunningRoutePoint> expectedRoutePoints = Arrays.asList(testRoutePoint);
        when(routePointRepository.findBySessionAndAltitudeRange(any(RunningSession.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(expectedRoutePoints);

        // when
        List<RunningRoutePoint> result = routePointDomainService.getSessionRoutePointsByAltitudeRange(
                testSession, minAltitude, maxAltitude);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(routePointRepository, times(1)).findBySessionAndAltitudeRange(testSession, minAltitude, maxAltitude);
    }

    @Test
    @DisplayName("특정 좌표 범위의 포인트 조회 테스트")
    void getSessionRoutePointsByCoordinateRange_ShouldReturnFilteredRoutePoints() {
        // given
        BigDecimal minLat = new BigDecimal("37.5");
        BigDecimal maxLat = new BigDecimal("37.6");
        BigDecimal minLng = new BigDecimal("126.9");
        BigDecimal maxLng = new BigDecimal("127.0");
        List<RunningRoutePoint> expectedRoutePoints = Arrays.asList(testRoutePoint);
        when(routePointRepository.findBySessionAndCoordinateRange(any(RunningSession.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(expectedRoutePoints);

        // when
        List<RunningRoutePoint> result = routePointDomainService.getSessionRoutePointsByCoordinateRange(
                testSession, minLat, maxLat, minLng, maxLng);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(routePointRepository, times(1)).findBySessionAndCoordinateRange(testSession, minLat, maxLat, minLng, maxLng);
    }

    @Test
    @DisplayName("세션의 시작 포인트 조회 테스트")
    void getSessionStartPoint_ShouldReturnStartPoint() {
        // given
        when(routePointRepository.findFirstBySessionOrderByTimestampAsc(any(RunningSession.class)))
                .thenReturn(Optional.of(testRoutePoint));

        // when
        Optional<RunningRoutePoint> result = routePointDomainService.getSessionStartPoint(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findFirstBySessionOrderByTimestampAsc(testSession);
    }

    @Test
    @DisplayName("세션의 종료 포인트 조회 테스트")
    void getSessionEndPoint_ShouldReturnEndPoint() {
        // given
        when(routePointRepository.findFirstBySessionOrderByTimestampDesc(any(RunningSession.class)))
                .thenReturn(Optional.of(testRoutePoint));

        // when
        Optional<RunningRoutePoint> result = routePointDomainService.getSessionEndPoint(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRoutePoint);
        verify(routePointRepository, times(1)).findFirstBySessionOrderByTimestampDesc(testSession);
    }

    @Test
    @DisplayName("세션의 경로 통계 정보 조회 테스트")
    void getSessionRoutePointStatistics_ShouldReturnStatistics() {
        // given
        BigDecimal maxSpeed = new BigDecimal("3.5");
        BigDecimal maxAltitude = new BigDecimal("80.0");
        BigDecimal minAltitude = new BigDecimal("20.0");
        Double averageSpeed = 2.8;
        Double averageAltitude = 45.5;
        long totalCount = 500L;

        RunningRoutePoint maxSpeedPoint = new RunningRoutePoint();
        maxSpeedPoint.setSpeed(maxSpeed);
        RunningRoutePoint maxAltitudePoint = new RunningRoutePoint();
        maxAltitudePoint.setAltitude(maxAltitude);
        RunningRoutePoint minAltitudePoint = new RunningRoutePoint();
        minAltitudePoint.setAltitude(minAltitude);

        when(routePointRepository.findMaxSpeedPointBySession(any(RunningSession.class))).thenReturn(Optional.of(maxSpeedPoint));
        when(routePointRepository.findMaxAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.of(maxAltitudePoint));
        when(routePointRepository.findMinAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.of(minAltitudePoint));
        when(routePointRepository.getAverageSpeedBySession(any(RunningSession.class))).thenReturn(averageSpeed);
        when(routePointRepository.getAverageAltitudeBySession(any(RunningSession.class))).thenReturn(averageAltitude);
        when(routePointRepository.countBySession(any(RunningSession.class))).thenReturn(totalCount);

        // when
        RunningRoutePointDomainService.RoutePointStatistics result = 
                routePointDomainService.getSessionRoutePointStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMaxSpeed()).isEqualTo(maxSpeed);
        assertThat(result.getMaxAltitude()).isEqualTo(maxAltitude);
        assertThat(result.getMinAltitude()).isEqualTo(minAltitude);
        assertThat(result.getAverageSpeed()).isEqualTo(averageSpeed);
        assertThat(result.getAverageAltitude()).isEqualTo(averageAltitude);
        assertThat(result.getTotalCount()).isEqualTo(totalCount);
    }

    @Test
    @DisplayName("경로 포인트 데이터가 없는 경우 통계 조회 테스트")
    void getSessionRoutePointStatistics_WithNoData_ShouldReturnNullValues() {
        // given
        when(routePointRepository.findMaxSpeedPointBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(routePointRepository.findMaxAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(routePointRepository.findMinAltitudePointBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(routePointRepository.getAverageSpeedBySession(any(RunningSession.class))).thenReturn(null);
        when(routePointRepository.getAverageAltitudeBySession(any(RunningSession.class))).thenReturn(null);
        when(routePointRepository.countBySession(any(RunningSession.class))).thenReturn(0L);

        // when
        RunningRoutePointDomainService.RoutePointStatistics result = 
                routePointDomainService.getSessionRoutePointStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMaxSpeed()).isNull();
        assertThat(result.getMaxAltitude()).isNull();
        assertThat(result.getMinAltitude()).isNull();
        assertThat(result.getAverageSpeed()).isNull();
        assertThat(result.getAverageAltitude()).isNull();
        assertThat(result.getTotalCount()).isEqualTo(0L);
    }
} 