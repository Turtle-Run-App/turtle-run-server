package com.turtleRun.be.running.domain.service;

import com.turtleRun.be.running.domain.entity.RunningSplit;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.running.domain.repository.RunningSplitRepository;
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
class RunningSplitDomainServiceTest {

    @Mock
    private RunningSplitRepository splitRepository;

    @InjectMocks
    private RunningSplitDomainService splitDomainService;

    private Player testPlayer;
    private RunningSession testSession;
    private RunningSplit testSplit;

    @BeforeEach
    void setUp() {
        testPlayer = Player.builder()
                .playerId(1L)
                .build();

        testSession = new RunningSession();
        testSession.setId(1L);
        testSession.setPlayer(testPlayer);
        testSession.setStartTime(LocalDateTime.now());

        testSplit = new RunningSplit();
        testSplit.setId(1L);
        testSplit.setSession(testSession);
        testSplit.setSplitDistance(1000);
        testSplit.setSplitDuration(360);
        testSplit.setSplitPace(new BigDecimal("6.0"));
        testSplit.setAverageHeartRate(150);
        testSplit.setStartTime(LocalDateTime.now());
        testSplit.setEndTime(LocalDateTime.now().plusMinutes(6));
    }

    @Test
    @DisplayName("스플릿 생성 테스트")
    void createSplit_ShouldCreateSplit() {
        // given
        Integer splitDistance = 1000;
        Integer splitDuration = 360;
        BigDecimal splitPace = new BigDecimal("6.0");
        Integer averageHeartRate = 150;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(6);

        when(splitRepository.save(any(RunningSplit.class))).thenReturn(testSplit);

        // when
        RunningSplit result = splitDomainService.createSplit(
                testSession, splitDistance, splitDuration, splitPace, averageHeartRate,
                startTime, endTime
        );

        // then
        assertThat(result).isNotNull();
        verify(splitRepository, times(1)).save(any(RunningSplit.class));
    }

    @Test
    @DisplayName("세션의 모든 스플릿 조회 테스트")
    void getSessionSplits_ShouldReturnSplits() {
        // given
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionOrderByStartTimeAsc(any(RunningSession.class))).thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplits(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findBySessionOrderByStartTimeAsc(testSession);
    }

    @Test
    @DisplayName("세션의 특정 기간 스플릿 조회 테스트")
    void getSessionSplitsByTimeRange_ShouldReturnSplits() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionAndTimeRange(any(RunningSession.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplitsByTimeRange(
                testSession, startTime, endTime);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(splitRepository, times(1)).findBySessionAndTimeRange(testSession, startTime, endTime);
    }

    @Test
    @DisplayName("세션의 최고 페이스 스플릿 조회 테스트")
    void getSessionBestPaceSplit_ShouldReturnBestPaceSplit() {
        // given
        when(splitRepository.findBestPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionBestPaceSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findBestPaceSplitBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최저 페이스 스플릿 조회 테스트")
    void getSessionWorstPaceSplit_ShouldReturnWorstPaceSplit() {
        // given
        when(splitRepository.findWorstPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionWorstPaceSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findWorstPaceSplitBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최고 심박수 스플릿 조회 테스트")
    void getSessionMaxHeartRateSplit_ShouldReturnMaxHeartRateSplit() {
        // given
        when(splitRepository.findMaxHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionMaxHeartRateSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findMaxHeartRateSplitBySession(testSession);
    }

    @Test
    @DisplayName("세션의 최저 심박수 스플릿 조회 테스트")
    void getSessionMinHeartRateSplit_ShouldReturnMinHeartRateSplit() {
        // given
        when(splitRepository.findMinHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionMinHeartRateSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findMinHeartRateSplitBySession(testSession);
    }

    @Test
    @DisplayName("세션의 평균 페이스 조회 테스트")
    void getSessionAveragePace_ShouldReturnAveragePace() {
        // given
        Double expectedAveragePace = 6.2;
        when(splitRepository.getAveragePaceBySession(any(RunningSession.class))).thenReturn(expectedAveragePace);

        // when
        Double result = splitDomainService.getSessionAveragePace(testSession);

        // then
        assertThat(result).isEqualTo(expectedAveragePace);
        verify(splitRepository, times(1)).getAveragePaceBySession(testSession);
    }

    @Test
    @DisplayName("세션의 평균 심박수 조회 테스트")
    void getSessionAverageHeartRate_ShouldReturnAverageHeartRate() {
        // given
        Double expectedAverageHeartRate = 155.5;
        when(splitRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(expectedAverageHeartRate);

        // when
        Double result = splitDomainService.getSessionAverageHeartRate(testSession);

        // then
        assertThat(result).isEqualTo(expectedAverageHeartRate);
        verify(splitRepository, times(1)).getAverageHeartRateBySession(testSession);
    }

    @Test
    @DisplayName("세션의 스플릿 개수 조회 테스트")
    void getSessionSplitCount_ShouldReturnCount() {
        // given
        long expectedCount = 10L;
        when(splitRepository.countBySession(any(RunningSession.class))).thenReturn(expectedCount);

        // when
        long result = splitDomainService.getSessionSplitCount(testSession);

        // then
        assertThat(result).isEqualTo(expectedCount);
        verify(splitRepository, times(1)).countBySession(testSession);
    }

    @Test
    @DisplayName("특정 거리 이상의 스플릿 조회 테스트")
    void getSessionSplitsByMinDistance_ShouldReturnFilteredSplits() {
        // given
        Integer minDistance = 800;
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionAndMinDistance(any(RunningSession.class), any(Integer.class)))
                .thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplitsByMinDistance(testSession, minDistance);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(splitRepository, times(1)).findBySessionAndMinDistance(testSession, minDistance);
    }

    @Test
    @DisplayName("특정 지속시간 이상의 스플릿 조회 테스트")
    void getSessionSplitsByMinDuration_ShouldReturnFilteredSplits() {
        // given
        Integer minDuration = 300;
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionAndMinDuration(any(RunningSession.class), any(Integer.class)))
                .thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplitsByMinDuration(testSession, minDuration);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(splitRepository, times(1)).findBySessionAndMinDuration(testSession, minDuration);
    }

    @Test
    @DisplayName("특정 페이스 범위의 스플릿 조회 테스트")
    void getSessionSplitsByPaceRange_ShouldReturnFilteredSplits() {
        // given
        BigDecimal minPace = new BigDecimal("5.5");
        BigDecimal maxPace = new BigDecimal("6.5");
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionAndPaceRange(any(RunningSession.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplitsByPaceRange(
                testSession, minPace, maxPace);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(splitRepository, times(1)).findBySessionAndPaceRange(testSession, minPace, maxPace);
    }

    @Test
    @DisplayName("특정 심박수 범위의 스플릿 조회 테스트")
    void getSessionSplitsByHeartRateRange_ShouldReturnFilteredSplits() {
        // given
        Integer minHeartRate = 140;
        Integer maxHeartRate = 160;
        List<RunningSplit> expectedSplits = Arrays.asList(testSplit);
        when(splitRepository.findBySessionAndHeartRateRange(any(RunningSession.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedSplits);

        // when
        List<RunningSplit> result = splitDomainService.getSessionSplitsByHeartRateRange(
                testSession, minHeartRate, maxHeartRate);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(splitRepository, times(1)).findBySessionAndHeartRateRange(testSession, minHeartRate, maxHeartRate);
    }

    @Test
    @DisplayName("세션의 첫 번째 스플릿 조회 테스트")
    void getSessionFirstSplit_ShouldReturnFirstSplit() {
        // given
        when(splitRepository.findFirstBySessionOrderByStartTimeAsc(any(RunningSession.class)))
                .thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionFirstSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findFirstBySessionOrderByStartTimeAsc(testSession);
    }

    @Test
    @DisplayName("세션의 마지막 스플릿 조회 테스트")
    void getSessionLastSplit_ShouldReturnLastSplit() {
        // given
        when(splitRepository.findFirstBySessionOrderByStartTimeDesc(any(RunningSession.class)))
                .thenReturn(Optional.of(testSplit));

        // when
        Optional<RunningSplit> result = splitDomainService.getSessionLastSplit(testSession);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSplit);
        verify(splitRepository, times(1)).findFirstBySessionOrderByStartTimeDesc(testSession);
    }

    @Test
    @DisplayName("세션의 스플릿 통계 정보 조회 테스트")
    void getSessionSplitStatistics_ShouldReturnStatistics() {
        // given
        BigDecimal bestPace = new BigDecimal("5.5");
        BigDecimal worstPace = new BigDecimal("7.0");
        Integer maxHeartRate = 180;
        Integer minHeartRate = 120;
        Double averagePace = 6.2;
        Double averageHeartRate = 155.5;
        long totalCount = 10L;

        RunningSplit bestPaceSplit = new RunningSplit();
        bestPaceSplit.setSplitPace(bestPace);
        RunningSplit worstPaceSplit = new RunningSplit();
        worstPaceSplit.setSplitPace(worstPace);
        RunningSplit maxHeartRateSplit = new RunningSplit();
        maxHeartRateSplit.setAverageHeartRate(maxHeartRate);
        RunningSplit minHeartRateSplit = new RunningSplit();
        minHeartRateSplit.setAverageHeartRate(minHeartRate);

        when(splitRepository.findBestPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(bestPaceSplit));
        when(splitRepository.findWorstPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(worstPaceSplit));
        when(splitRepository.findMaxHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(maxHeartRateSplit));
        when(splitRepository.findMinHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.of(minHeartRateSplit));
        when(splitRepository.getAveragePaceBySession(any(RunningSession.class))).thenReturn(averagePace);
        when(splitRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(averageHeartRate);
        when(splitRepository.countBySession(any(RunningSession.class))).thenReturn(totalCount);

        // when
        RunningSplitDomainService.SplitStatistics result = 
                splitDomainService.getSessionSplitStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBestPace()).isEqualTo(bestPace);
        assertThat(result.getWorstPace()).isEqualTo(worstPace);
        assertThat(result.getMaxHeartRate()).isEqualTo(maxHeartRate);
        assertThat(result.getMinHeartRate()).isEqualTo(minHeartRate);
        assertThat(result.getAveragePace()).isEqualTo(averagePace);
        assertThat(result.getAverageHeartRate()).isEqualTo(averageHeartRate);
        assertThat(result.getTotalCount()).isEqualTo(totalCount);
    }

    @Test
    @DisplayName("스플릿 데이터가 없는 경우 통계 조회 테스트")
    void getSessionSplitStatistics_WithNoData_ShouldReturnNullValues() {
        // given
        when(splitRepository.findBestPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(splitRepository.findWorstPaceSplitBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(splitRepository.findMaxHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(splitRepository.findMinHeartRateSplitBySession(any(RunningSession.class))).thenReturn(Optional.empty());
        when(splitRepository.getAveragePaceBySession(any(RunningSession.class))).thenReturn(null);
        when(splitRepository.getAverageHeartRateBySession(any(RunningSession.class))).thenReturn(null);
        when(splitRepository.countBySession(any(RunningSession.class))).thenReturn(0L);

        // when
        RunningSplitDomainService.SplitStatistics result = 
                splitDomainService.getSessionSplitStatistics(testSession);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBestPace()).isNull();
        assertThat(result.getWorstPace()).isNull();
        assertThat(result.getMaxHeartRate()).isNull();
        assertThat(result.getMinHeartRate()).isNull();
        assertThat(result.getAveragePace()).isNull();
        assertThat(result.getAverageHeartRate()).isNull();
        assertThat(result.getTotalCount()).isEqualTo(0L);
    }
} 