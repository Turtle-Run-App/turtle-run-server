package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningHeartRate;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningHeartRateRepository;
import com.turtleRun.be.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningHeartRateDomainServiceTest {

    @Mock
    private RunningHeartRateRepository heartRateRepository;

    @InjectMocks
    private RunningHeartRateDomainService heartRateDomainService;

    private Player testPlayer;
    private RunningSession testSession;
    private RunningHeartRate testHeartRate;

    @BeforeEach
    void setUp() {
        testPlayer = Player.builder()
                .playerId(1L)
                .build();

        testSession = new RunningSession();
        testSession.setId(1L);
        testSession.setPlayer(testPlayer);
        testSession.setStartTime(LocalDateTime.now());

        testHeartRate = new RunningHeartRate();
        testHeartRate.setId(1L);
        testHeartRate.setSession(testSession);
        testHeartRate.setHeartRate(150);
        testHeartRate.setTimestamp(LocalDateTime.now());
    }

    @Test
    @DisplayName("심박수 데이터 생성 테스트")
    void createHeartRate_ShouldCreateHeartRate() {
        // given
        Integer heartRate = 150;
        LocalDateTime timestamp = LocalDateTime.now();
        when(heartRateRepository.save(any(RunningHeartRate.class))).thenReturn(testHeartRate);

        // when
        RunningHeartRate result = heartRateDomainService.createHeartRate(testSession, heartRate, timestamp);

        // then
        assertThat(result).isNotNull();
        verify(heartRateRepository, times(1)).save(any(RunningHeartRate.class));
    }

    @Test
    @DisplayName("세션의 모든 심박수 데이터 조회 테스트")
    void getSessionHeartRates_ShouldReturnHeartRates() {
        // given
        List<RunningHeartRate> expectedHeartRates = Arrays.asList(testHeartRate);
        when(heartRateRepository.findBySessionOrderByTimestampAsc(any(RunningSession.class))).thenReturn(expectedHeartRates);

        // when
        List<RunningHeartRate> result = heartRateDomainService.getSessionHeartRates(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testHeartRate);
        verify(heartRateRepository, times(1)).findBySessionOrderByTimestampAsc(testSession);
    }

    @Test
    @DisplayName("세션의 특정 기간 심박수 데이터 조회 테스트")
    void getSessionHeartRatesByTimeRange_ShouldReturnHeartRates() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        List<RunningHeartRate> expectedHeartRates = Arrays.asList(testHeartRate);
        when(heartRateRepository.findBySessionAndTimeRange(any(RunningSession.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedHeartRates);

        // when
        List<RunningHeartRate> result = heartRateDomainService.getSessionHeartRatesByTimeRange(
                testSession, startTime, endTime);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(heartRateRepository, times(1)).findBySessionAndTimeRange(testSession, startTime, endTime);
    }

    @Test
    @DisplayName("세션의 최고 심박수 데이터 조회 테스트")
    void getSessionMaxHeartRate_ShouldReturnMaxHeartRate() {
        // given
        when(heartRateRepository.findMaxHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.of(testHeartRate));

        // when
        Optional<RunningHeartRate> result = heartRateDomainService.getSessionMaxHeartRate(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testHeartRate);
        verify(heartRateRepository, times(1)).findMaxHeartRateBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최저 심박수 데이터 조회 테스트")
    void getSessionMinHeartRate_ShouldReturnMinHeartRate() {
        // given
        when(heartRateRepository.findMinHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.of(testHeartRate));

        // when
        Optional<RunningHeartRate> result = heartRateDomainService.getSessionMinHeartRate(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testHeartRate);
        verify(heartRateRepository, times(1)).findMinHeartRateBySession(testSession);
    }

    @Test
    @DisplayName("세션의 평균 심박수 조회 테스트")
    void getSessionAverageHeartRate_ShouldReturnAverage() {
        // given
        Double expectedAverage = 155.5;
        when(heartRateRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(expectedAverage);

        // when
        Double result = heartRateDomainService.getSessionAverageHeartRate(testSession);

        // then
        assertThat(result).isEqualTo(expectedAverage);
        verify(heartRateRepository, times(1)).getAverageHeartRateBySession(testSession);
    }

    @Test
    @DisplayName("세션의 심박수 데이터 개수 조회 테스트")
    void getSessionHeartRateCount_ShouldReturnCount() {
        // given
        long expectedCount = 100L;
        when(heartRateRepository.countBySession(any(RunningSession.class))).thenReturn(expectedCount);

        // when
        long result = heartRateDomainService.getSessionHeartRateCount(testSession);

        // then
        assertThat(result).isEqualTo(expectedCount);
        verify(heartRateRepository, times(1)).countBySession(testSession);
    }

    @Test
    @DisplayName("특정 심박수 범위의 데이터 조회 테스트")
    void getSessionHeartRatesByRange_ShouldReturnFilteredHeartRates() {
        // given
        Integer minHeartRate = 140;
        Integer maxHeartRate = 160;
        List<RunningHeartRate> expectedHeartRates = Arrays.asList(testHeartRate);
        when(heartRateRepository.findBySessionAndHeartRateRange(any(RunningSession.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedHeartRates);

        // when
        List<RunningHeartRate> result = heartRateDomainService.getSessionHeartRatesByRange(
                testSession, minHeartRate, maxHeartRate);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(heartRateRepository, times(1)).findBySessionAndHeartRateRange(testSession, minHeartRate, maxHeartRate);
    }

    @Test
    @DisplayName("세션의 특정 시간 이후 심박수 데이터 조회 테스트")
    void getSessionHeartRatesFromTime_ShouldReturnHeartRates() {
        // given
        LocalDateTime fromTime = LocalDateTime.now().minusMinutes(30);
        List<RunningHeartRate> expectedHeartRates = Arrays.asList(testHeartRate);
        when(heartRateRepository.findBySessionAndFromTime(any(RunningSession.class), any(LocalDateTime.class)))
                .thenReturn(expectedHeartRates);

        // when
        List<RunningHeartRate> result = heartRateDomainService.getSessionHeartRatesFromTime(testSession, fromTime);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(heartRateRepository, times(1)).findBySessionAndFromTime(testSession, fromTime);
    }

    @Test
    @DisplayName("세션의 심박수 통계 정보 조회 테스트")
    void getSessionHeartRateStatistics_ShouldReturnStatistics() {
        // given
        Integer maxHeartRate = 180;
        Integer minHeartRate = 120;
        Double averageHeartRate = 155.5;
        long totalCount = 100L;

        RunningHeartRate maxHeartRateData = new RunningHeartRate();
        maxHeartRateData.setHeartRate(maxHeartRate);
        RunningHeartRate minHeartRateData = new RunningHeartRate();
        minHeartRateData.setHeartRate(minHeartRate);

        when(heartRateRepository.findMaxHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.of(maxHeartRateData));
        when(heartRateRepository.findMinHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.of(minHeartRateData));
        when(heartRateRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(averageHeartRate);
        when(heartRateRepository.countBySession(any(RunningSession.class))).thenReturn(totalCount);

        // when
        RunningHeartRateDomainService.HeartRateStatistics result = 
                heartRateDomainService.getSessionHeartRateStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMaxHeartRate()).isEqualTo(maxHeartRate);
        assertThat(result.getMinHeartRate()).isEqualTo(minHeartRate);
        assertThat(result.getAverageHeartRate()).isEqualTo(averageHeartRate);
        assertThat(result.getTotalCount()).isEqualTo(totalCount);
    }

    @Test
    @DisplayName("심박수 데이터가 없는 경우 통계 조회 테스트")
    void getSessionHeartRateStatistics_WithNoData_ShouldReturnNullValues() {
        // given
        when(heartRateRepository.findMaxHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(heartRateRepository.findMinHeartRateBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(heartRateRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(null);
        when(heartRateRepository.countBySession(any(RunningSession.class))).thenReturn(0L);

        // when
        RunningHeartRateDomainService.HeartRateStatistics result = 
                heartRateDomainService.getSessionHeartRateStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMaxHeartRate()).isNull();
        assertThat(result.getMinHeartRate()).isNull();
        assertThat(result.getAverageHeartRate()).isNull();
        assertThat(result.getTotalCount()).isEqualTo(0L);
    }
} 