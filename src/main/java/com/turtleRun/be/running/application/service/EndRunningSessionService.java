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
import com.turtleRun.be.player.domain.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 러닝 세션 종료를 처리하는 애플리케이션 서비스
 * iOS 클라이언트로부터 받은 러닝 완료 데이터를 처리하고 저장합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EndRunningSessionService {

    private final RunningSessionRepository runningSessionRepository;
    private final RunningSessionDomainService runningSessionDomainService;
    private final RunningHeartRateDomainService heartRateDomainService;
    private final RunningRoutePointDomainService routePointDomainService;
    private final RunningSplitDomainService splitDomainService;

    /**
     * 러닝 세션을 완료하고 관련 데이터를 저장합니다.
     *
     * @param request 러닝 세션 완료 요청 데이터
     * @return 완료된 러닝 세션
     */
    public RunningSession endRunningSession(EndRunningSessionRequest request) {
        log.info("러닝 세션 완료 요청: playerId={}", request.getPlayerId());

        // 1. 새로운 러닝 세션 생성
        RunningSession session = new RunningSession();
        
        // Player 객체 생성 및 설정
        Player player = new Player();
        player.setPlayerId(request.getPlayerId());
        session.setPlayer(player);
        
        session.setStartTime(request.getEndTime().minusSeconds(request.getDuration()));
        session.setEndTime(request.getEndTime());
        session.setDuration(request.getDuration());
        session.setActiveDuration(request.getActiveDuration());
        session.setDistance(request.getDistance());
        session.setTotalCalories(request.getTotalCalories());
        session.setAveragePace(request.getAveragePace());
        session.setBestPace(request.getBestPace());
        session.setAverageSpeed(request.getAverageSpeed());
        session.setMaxSpeed(request.getMaxSpeed());
        session.setTotalAscent(request.getTotalAscent());
        session.setTotalDescent(request.getTotalDescent());
        session.setWeatherTemperature(request.getWeatherTemperature());
        session.setWeatherHumidity(request.getWeatherHumidity());
        session.setSourceDevice(request.getSourceDevice());
        session.setSourceApp(request.getSourceApp());

        // 2. 세션 저장
        RunningSession savedSession = runningSessionRepository.save(session);

        // 3. 심박수 데이터 저장
        if (request.getHeartRateData() != null && !request.getHeartRateData().isEmpty()) {
            saveHeartRateData(savedSession, request.getHeartRateData());
        }

        // 4. 경로 포인트 데이터 저장
        if (request.getRoutePoints() != null && !request.getRoutePoints().isEmpty()) {
            saveRoutePointData(savedSession, request.getRoutePoints());
        }

        // 5. 스플릿 데이터 저장
        if (request.getSplits() != null && !request.getSplits().isEmpty()) {
            saveSplitData(savedSession, request.getSplits());
        }

        // 6. 세션 통계 생성 및 저장
        if (request.getStatistics() != null) {
            saveSessionStatistics(savedSession, request.getStatistics());
        }

        log.info("러닝 세션 완료 완료: sessionId={}, distance={}m, duration={}s", 
                savedSession.getId(), savedSession.getDistance(), savedSession.getDuration());

        return savedSession;
    }

    /**
     * 심박수 데이터를 저장합니다.
     */
    private void saveHeartRateData(RunningSession session, List<HeartRateData> heartRateDataList) {
        for (HeartRateData data : heartRateDataList) {
            heartRateDomainService.createHeartRate(
                    session,
                    data.getHeartRate(),
                    data.getTimestamp()
            );
        }
        log.debug("심박수 데이터 저장 완료: {}개", heartRateDataList.size());
    }

    /**
     * 경로 포인트 데이터를 저장합니다.
     */
    private void saveRoutePointData(RunningSession session, List<RoutePointData> routePointDataList) {
        for (RoutePointData data : routePointDataList) {
            routePointDomainService.createRoutePoint(
                    session,
                    data.getLatitude(),
                    data.getLongitude(),
                    data.getAltitude(),
                    data.getTimestamp(),
                    data.getSpeed(),
                    data.getVerticalAccuracy(),
                    data.getHorizontalAccuracy(),
                    data.getCourse()
            );
        }
        log.debug("경로 포인트 데이터 저장 완료: {}개", routePointDataList.size());
    }

    /**
     * 스플릿 데이터를 저장합니다.
     */
    private void saveSplitData(RunningSession session, List<SplitData> splitDataList) {
        for (SplitData data : splitDataList) {
            splitDomainService.createSplit(
                    session,
                    data.getSplitDistance(),
                    data.getSplitDuration(),
                    data.getSplitPace(),
                    data.getAverageHeartRate(),
                    data.getStartTime(),
                    data.getEndTime()
            );
        }
        log.debug("스플릿 데이터 저장 완료: {}개", splitDataList.size());
    }

    /**
     * 세션 통계를 저장합니다.
     */
    private void saveSessionStatistics(RunningSession session, StatisticsData statistics) {
        runningSessionDomainService.createRunningSessionStatistics(
                session,
                statistics.getAverageStrideLength(),
                statistics.getAverageGroundContactTime(),
                statistics.getAverageVerticalOscillation(),
                statistics.getAveragePower(),
                statistics.getAverageCadence(),
                statistics.getMaxHeartRate(),
                statistics.getAverageHeartRate(),
                statistics.getTrainingEffect(),
                statistics.getVo2max()
        );
        log.debug("세션 통계 저장 완료");
    }

} 