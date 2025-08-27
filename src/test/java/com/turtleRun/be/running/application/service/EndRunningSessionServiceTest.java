package com.turtleRun.be.running.application.service;

import com.turtleRun.be.running.application.dto.EndRunningSessionRequest;
import com.turtleRun.be.running.application.dto.HeartRateData;
import com.turtleRun.be.running.application.dto.RoutePointData;
import com.turtleRun.be.running.application.dto.SplitData;
import com.turtleRun.be.running.application.dto.StatisticsData;
import com.turtleRun.be.running.domain.entity.RunningHeartRate;
import com.turtleRun.be.running.domain.entity.RunningRoutePoint;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.entity.RunningSplit;
import com.turtleRun.be.running.domain.repository.RunningSessionRepository;
import com.turtleRun.be.running.domain.service.RunningHeartRateDomainService;
import com.turtleRun.be.running.domain.service.RunningRoutePointDomainService;
import com.turtleRun.be.running.domain.service.RunningSessionDomainService;
import com.turtleRun.be.running.domain.service.RunningSplitDomainService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndRunningSessionServiceTest {

    @Mock
    private RunningSessionRepository runningSessionRepository;

    @Mock
    private RunningSessionDomainService runningSessionDomainService;

    @Mock
    private RunningHeartRateDomainService heartRateDomainService;

    @Mock
    private RunningRoutePointDomainService routePointDomainService;

    @Mock
    private RunningSplitDomainService splitDomainService;

    @InjectMocks
    private EndRunningSessionService endRunningSessionService;

    private EndRunningSessionRequest mockRequest;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.of(2024, 1, 1, 10, 0, 0);

        mockRequest = EndRunningSessionRequest.builder()
                .playerId(100L)
                .endTime(testTime)
                .duration(3600)
                .activeDuration(3500)
                .distance(new BigDecimal("10000.0"))
                .totalCalories(new BigDecimal("800.0"))
                .averagePace(new BigDecimal("5.5"))
                .bestPace(new BigDecimal("4.8"))
                .averageSpeed(new BigDecimal("2.8"))
                .maxSpeed(new BigDecimal("3.5"))
                .totalAscent(new BigDecimal("150.0"))
                .totalDescent(new BigDecimal("120.0"))
                .weatherTemperature(new BigDecimal("22.5"))
                .weatherHumidity(65)
                .sourceDevice("iPhone 15")
                .sourceApp("TurtleRun v1.0")
                .build();
    }

    @Test
    @DisplayName("러닝 세션을 성공적으로 완료할 수 있다")
    void shouldEndRunningSessionSuccessfully() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);
        savedSession.setStartTime(testTime.minusSeconds(3600));
        savedSession.setEndTime(testTime);
        savedSession.setDuration(3600);
        savedSession.setDistance(new BigDecimal("10000.0"));

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        RunningSession result = endRunningSessionService.endRunningSession(mockRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDistance()).isEqualTo(new BigDecimal("10000.0"));
        
        verify(runningSessionRepository).save(any(RunningSession.class));
    }



    @Test
    @DisplayName("심박수 데이터와 함께 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithHeartRateData() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        List<HeartRateData> heartRateDataList = Arrays.asList(
                HeartRateData.builder()
                        .heartRate(150)
                        .timestamp(testTime.minusMinutes(30))
                        .build(),
                HeartRateData.builder()
                        .heartRate(160)
                        .timestamp(testTime.minusMinutes(15))
                        .build()
        );

        mockRequest.setHeartRateData(heartRateDataList);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        endRunningSessionService.endRunningSession(mockRequest);

        // then
        verify(heartRateDomainService, times(2)).createHeartRate(
                eq(savedSession), anyInt(), any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("경로 포인트 데이터와 함께 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithRoutePointData() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        List<RoutePointData> routePointDataList = Arrays.asList(
                RoutePointData.builder()
                        .latitude(new BigDecimal("37.5665"))
                        .longitude(new BigDecimal("126.9780"))
                        .altitude(new BigDecimal("50.0"))
                        .timestamp(testTime.minusMinutes(30))
                        .speed(new BigDecimal("2.5"))
                        .verticalAccuracy(new BigDecimal("5.0"))
                        .horizontalAccuracy(new BigDecimal("3.0"))
                        .course(new BigDecimal("180.0"))
                        .build(),
                RoutePointData.builder()
                        .latitude(new BigDecimal("37.5666"))
                        .longitude(new BigDecimal("126.9781"))
                        .altitude(new BigDecimal("52.0"))
                        .timestamp(testTime.minusMinutes(15))
                        .speed(new BigDecimal("2.8"))
                        .verticalAccuracy(new BigDecimal("5.0"))
                        .horizontalAccuracy(new BigDecimal("3.0"))
                        .course(new BigDecimal("185.0"))
                        .build()
        );

        mockRequest.setRoutePoints(routePointDataList);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        endRunningSessionService.endRunningSession(mockRequest);

        // then
        verify(routePointDomainService, times(2)).createRoutePoint(
                eq(savedSession), any(BigDecimal.class), any(BigDecimal.class),
                any(BigDecimal.class), any(LocalDateTime.class), any(BigDecimal.class),
                any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)
        );
    }

    @Test
    @DisplayName("스플릿 데이터와 함께 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithSplitData() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        List<SplitData> splitDataList = Arrays.asList(
                SplitData.builder()
                        .splitDistance(1000)
                        .splitDuration(300)
                        .splitPace(new BigDecimal("5.0"))
                        .averageHeartRate(155)
                        .startTime(testTime.minusMinutes(30))
                        .endTime(testTime.minusMinutes(25))
                        .build(),
                SplitData.builder()
                        .splitDistance(1000)
                        .splitDuration(320)
                        .splitPace(new BigDecimal("5.3"))
                        .averageHeartRate(160)
                        .startTime(testTime.minusMinutes(25))
                        .endTime(testTime.minusMinutes(20))
                        .build()
        );

        mockRequest.setSplits(splitDataList);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        endRunningSessionService.endRunningSession(mockRequest);

        // then
        verify(splitDomainService, times(2)).createSplit(
                eq(savedSession), anyInt(), anyInt(), any(BigDecimal.class),
                anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)
        );
    }

    @Test
    @DisplayName("통계 데이터와 함께 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithStatisticsData() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        StatisticsData statisticsData = StatisticsData.builder()
                .averageStrideLength(new BigDecimal("1.2"))
                .averageGroundContactTime(new BigDecimal("250.0"))
                .averageVerticalOscillation(new BigDecimal("8.5"))
                .averagePower(new BigDecimal("280.0"))
                .averageCadence(175)
                .maxHeartRate(185)
                .averageHeartRate(160)
                .trainingEffect(new BigDecimal("3.5"))
                .vo2max(new BigDecimal("52.0"))
                .build();

        mockRequest.setStatistics(statisticsData);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        endRunningSessionService.endRunningSession(mockRequest);

        // then
        verify(runningSessionDomainService).createRunningSessionStatistics(
                eq(savedSession),
                eq(new BigDecimal("1.2")), eq(new BigDecimal("250.0")),
                eq(new BigDecimal("8.5")), eq(new BigDecimal("280.0")),
                eq(175), eq(185), eq(160),
                eq(new BigDecimal("3.5")), eq(new BigDecimal("52.0"))
        );
    }

    @Test
    @DisplayName("빈 데이터 리스트가 있어도 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithEmptyDataLists() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        mockRequest.setHeartRateData(Arrays.asList());
        mockRequest.setRoutePoints(Arrays.asList());
        mockRequest.setSplits(Arrays.asList());
        mockRequest.setStatistics(null);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        RunningSession result = endRunningSessionService.endRunningSession(mockRequest);

        // then
        assertThat(result).isNotNull();
        verifyNoInteractions(heartRateDomainService);
        verifyNoInteractions(routePointDomainService);
        verifyNoInteractions(splitDomainService);
    }

    @Test
    @DisplayName("null 데이터가 있어도 러닝 세션을 완료할 수 있다")
    void shouldEndRunningSessionWithNullData() {
        // given
        RunningSession savedSession = new RunningSession();
        savedSession.setId(1L);

        mockRequest.setHeartRateData(null);
        mockRequest.setRoutePoints(null);
        mockRequest.setSplits(null);
        mockRequest.setStatistics(null);

        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(savedSession);

        // when
        RunningSession result = endRunningSessionService.endRunningSession(mockRequest);

        // then
        assertThat(result).isNotNull();
        verifyNoInteractions(heartRateDomainService);
        verifyNoInteractions(routePointDomainService);
        verifyNoInteractions(splitDomainService);
    }
} 