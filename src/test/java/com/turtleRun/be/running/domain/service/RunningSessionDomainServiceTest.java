package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.entity.RunningSessionStatistics;
import com.turtleRun.be.running.domain.repository.RunningSessionRepository;
import com.turtleRun.be.running.domain.repository.RunningSessionStatisticsRepository;
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
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningSessionDomainServiceTest {

    @Mock
    private RunningSessionRepository runningSessionRepository;

    @Mock
    private RunningSessionStatisticsRepository statisticsRepository;

    @InjectMocks
    private RunningSessionDomainService runningSessionDomainService;

    private Player testPlayer;
    private RunningSession testSession;
    private RunningSessionStatistics testStatistics;

    @BeforeEach
    void setUp() {
        testPlayer = Player.builder()
                .playerId(1L)
                .build();

        testSession = new RunningSession();
        testSession.setId(1L);
        testSession.setPlayer(testPlayer);
        testSession.setStartTime(LocalDateTime.now());
        testSession.setDistance(new BigDecimal("5000.0"));
        testSession.setDuration(1800);
        testSession.setAveragePace(new BigDecimal("6.0"));

        testStatistics = new RunningSessionStatistics();
        testStatistics.setId(1L);
        testStatistics.setSession(testSession);
        testStatistics.setAverageHeartRate(150);
        testStatistics.setMaxHeartRate(180);
        testStatistics.setAverageCadence(170);
    }

    @Test
    @DisplayName("러닝 세션 생성 테스트")
    void createRunningSession_ShouldCreateSession() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(testSession);

        // when
        RunningSession result = runningSessionDomainService.createRunningSession(testPlayer, startTime);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlayer()).isEqualTo(testPlayer);
//        assertThat(result.getStartTime()).isEqualTo(startTime);
//        assertThat(ChronoLocalDateTime.timeLineOrder());
        verify(runningSessionRepository, times(1)).save(any(RunningSession.class));
    }

    @Test
    @DisplayName("러닝 세션 완료 테스트")
    void completeRunningSession_ShouldCompleteSession() {
        // given
        Long sessionId = 1L;
        LocalDateTime endTime = LocalDateTime.now();
        Integer duration = 1800;
        Integer activeDuration = 1750;
        BigDecimal distance = new BigDecimal("5000.0");
        BigDecimal totalCalories = new BigDecimal("400.0");
        BigDecimal averagePace = new BigDecimal("6.0");
        BigDecimal bestPace = new BigDecimal("5.5");
        BigDecimal averageSpeed = new BigDecimal("2.78");
        BigDecimal maxSpeed = new BigDecimal("3.5");
        BigDecimal totalAscent = new BigDecimal("50.0");
        BigDecimal totalDescent = new BigDecimal("45.0");
        BigDecimal weatherTemperature = new BigDecimal("20.0");
        Integer weatherHumidity = 60;
        String sourceDevice = "Garmin Forerunner";
        String sourceApp = "Garmin Connect";

        when(runningSessionRepository.findById(anyLong())).thenReturn(Optional.of(testSession));
        when(runningSessionRepository.save(any(RunningSession.class))).thenReturn(testSession);

        // when
        RunningSession result = runningSessionDomainService.completeRunningSession(
                sessionId, endTime, duration, activeDuration, distance, totalCalories,
                averagePace, bestPace, averageSpeed, maxSpeed, totalAscent, totalDescent,
                weatherTemperature, weatherHumidity, sourceDevice, sourceApp
        );

        // then
        assertThat(result).isNotNull();
        verify(runningSessionRepository, times(1)).findById(sessionId);
        verify(runningSessionRepository, times(1)).save(any(RunningSession.class));
    }

    @Test
    @DisplayName("플레이어의 러닝 세션 목록 조회 테스트")
    void getPlayerRunningSessions_ShouldReturnSessions() {
        // given
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerOrderByStartTimeDesc(any(Player.class))).thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessions(testPlayer);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testSession);
        verify(runningSessionRepository, times(1)).findByPlayerOrderByStartTimeDesc(testPlayer);
    }

    @Test
    @DisplayName("플레이어의 특정 기간 러닝 세션 조회 테스트")
    void getPlayerRunningSessionsByDateRange_ShouldReturnSessions() {
        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerAndDateRange(any(Player.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessionsByDateRange(
                testPlayer, startDate, endDate);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(runningSessionRepository, times(1)).findByPlayerAndDateRange(testPlayer, startDate, endDate);
    }

    @Test
    @DisplayName("플레이어의 최근 러닝 세션 조회 테스트")
    void getPlayerLatestRunningSession_ShouldReturnLatestSession() {
        // given
        when(runningSessionRepository.findFirstByPlayerOrderByStartTimeDesc(any(Player.class)))
                .thenReturn(Optional.of(testSession));

        // when
        Optional<RunningSession> result = runningSessionDomainService.getPlayerLatestRunningSession(testPlayer);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSession);
        verify(runningSessionRepository, times(1)).findFirstByPlayerOrderByStartTimeDesc(testPlayer);
    }

    @Test
    @DisplayName("플레이어의 특정 거리 이상 러닝 세션 조회 테스트")
    void getPlayerRunningSessionsByMinDistance_ShouldReturnSessions() {
        // given
        BigDecimal minDistance = new BigDecimal("3000.0");
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerAndMinDistance(any(Player.class), any(java.math.BigDecimal.class)))
                .thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessionsByMinDistance(
                testPlayer, minDistance);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(runningSessionRepository, times(1)).findByPlayerAndMinDistance(testPlayer, minDistance);
    }

    @Test
    @DisplayName("플레이어의 특정 지속시간 이상 러닝 세션 조회 테스트")
    void getPlayerRunningSessionsByMinDuration_ShouldReturnSessions() {
        // given
        Integer minDuration = 1800;
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerAndMinDuration(any(Player.class), any(Integer.class)))
                .thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessionsByMinDuration(
                testPlayer, minDuration);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(runningSessionRepository, times(1)).findByPlayerAndMinDuration(testPlayer, minDuration);
    }

    @Test
    @DisplayName("플레이어의 최고 페이스 러닝 세션 조회 테스트")
    void getPlayerRunningSessionsByBestPace_ShouldReturnSessions() {
        // given
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerOrderByBestPaceAsc(any(Player.class)))
                .thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessionsByBestPace(testPlayer);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(runningSessionRepository, times(1)).findByPlayerOrderByBestPaceAsc(testPlayer);
    }

    @Test
    @DisplayName("플레이어의 최장 거리 러닝 세션 조회 테스트")
    void getPlayerRunningSessionsByDistance_ShouldReturnSessions() {
        // given
        List<RunningSession> expectedSessions = Arrays.asList(testSession);
        when(runningSessionRepository.findByPlayerOrderByDistanceDesc(any(Player.class)))
                .thenReturn(expectedSessions);

        // when
        List<RunningSession> result = runningSessionDomainService.getPlayerRunningSessionsByDistance(testPlayer);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(runningSessionRepository, times(1)).findByPlayerOrderByDistanceDesc(testPlayer);
    }

    @Test
    @DisplayName("플레이어의 러닝 통계 조회 테스트")
    void getPlayerRunningStatistics_ShouldReturnStatistics() {
        // given
        long totalSessions = 10L;
        BigDecimal totalDistance = new BigDecimal("50000.0");
        Integer totalDuration = 18000;

        when(runningSessionRepository.countByPlayer(any(Player.class))).thenReturn(totalSessions);
        when(runningSessionRepository.getTotalDistanceByPlayer(any(Player.class))).thenReturn(totalDistance);
        when(runningSessionRepository.getTotalDurationByPlayer(any(Player.class))).thenReturn(totalDuration);

        // when
        RunningSessionDomainService.PlayerRunningStatistics result = 
                runningSessionDomainService.getPlayerRunningStatistics(testPlayer);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalSessions()).isEqualTo(totalSessions);
        assertThat(result.getTotalDistance()).isEqualTo(totalDistance);
        assertThat(result.getTotalDuration()).isEqualTo(totalDuration);
        verify(runningSessionRepository, times(1)).countByPlayer(testPlayer);
        verify(runningSessionRepository, times(1)).getTotalDistanceByPlayer(testPlayer);
        verify(runningSessionRepository, times(1)).getTotalDurationByPlayer(testPlayer);
    }

    @Test
    @DisplayName("러닝 세션 통계 생성 테스트")
    void createRunningSessionStatistics_ShouldCreateStatistics() {
        // given
        BigDecimal averageStrideLength = new BigDecimal("1.2");
        BigDecimal averageGroundContactTime = new BigDecimal("250.0");
        BigDecimal averageVerticalOscillation = new BigDecimal("8.5");
        BigDecimal averagePower = new BigDecimal("300.0");
        Integer averageCadence = 170;
        Integer maxHeartRate = 180;
        Integer averageHeartRate = 150;
        BigDecimal trainingEffect = new BigDecimal("3.5");
        BigDecimal vo2max = new BigDecimal("55.0");

        when(statisticsRepository.save(any(RunningSessionStatistics.class))).thenReturn(testStatistics);

        // when
        RunningSessionStatistics result = runningSessionDomainService.createRunningSessionStatistics(
                testSession, averageStrideLength, averageGroundContactTime, averageVerticalOscillation,
                averagePower, averageCadence, maxHeartRate, averageHeartRate, trainingEffect, vo2max
        );

        // then
        assertThat(result).isNotNull();
        verify(statisticsRepository, times(1)).save(any(RunningSessionStatistics.class));
    }

    @Test
    @DisplayName("러닝 세션 통계 조회 테스트")
    void getRunningSessionStatistics_ShouldReturnStatistics() {
        // given
        when(statisticsRepository.findBySession(any(RunningSession.class))).thenReturn(Optional.of(testStatistics));

        // when
        Optional<RunningSessionStatistics> result = runningSessionDomainService.getRunningSessionStatistics(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testStatistics);
        verify(statisticsRepository, times(1)).findBySession(testSession);
    }

    @Test
    @DisplayName("존재하지 않는 세션으로 완료 시도 시 예외 발생 테스트")
    void completeRunningSession_WithNonExistentSession_ShouldThrowException() {
        // given
        Long nonExistentSessionId = 999L;
        when(runningSessionRepository.findById(nonExistentSessionId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> runningSessionDomainService.completeRunningSession(
                nonExistentSessionId, LocalDateTime.now(), 1800, 1750,
                new BigDecimal("5000.0"), new BigDecimal("400.0"),
                new BigDecimal("6.0"), new BigDecimal("5.5"),
                new BigDecimal("2.78"), new BigDecimal("3.5"),
                new BigDecimal("50.0"), new BigDecimal("45.0"),
                new BigDecimal("20.0"), 60, "Garmin", "Connect"
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Running session not found");

        verify(runningSessionRepository, times(1)).findById(nonExistentSessionId);
        verify(runningSessionRepository, never()).save(any(RunningSession.class));
    }
} 