package com.turtleRun.be.controller;

import com.turtleRun.be.common.exception.BulkInitialException;
import com.turtleRun.be.common.exception.BusinessException;
import com.turtleRun.be.common.model.SaveBulkInitialRequestDto;
import com.turtleRun.be.common.model.SaveBulkInitialResponseDto;
import com.turtleRun.be.running.application.service.EndRunningSessionService;
import com.turtleRun.be.running.application.dto.EndRunningSessionRequest;
import com.turtleRun.be.running.application.dto.RoutePointData;
import com.turtleRun.be.running.domain.entity.RunningSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 신규 사용자의 러닝 데이터 초기 동기화를 위한 Controller
 * 사용자 가입 시점부터 30일의 러닝 데이터를 대량으로 동기화
 */
@Slf4j
@RestController
@RequestMapping("api/bulkInitial")
@RequiredArgsConstructor
@Tag(name = "Bulk Initial Sync", description = "신규 사용자 러닝 데이터 초기 동기화 API")
public class BulkInitialController {

    private final EndRunningSessionService endRunningSessionService;

    @PostMapping
    @Operation(
        summary = "러닝 데이터 초기 동기화",
        description = "신규 사용자의 가입 시점부터 30일의 러닝 데이터를 대량으로 동기화합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "러닝 데이터 동기화 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SaveBulkInitialResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SaveBulkInitialResponseDto.class)
            )
        )
    })
    public ResponseEntity<SaveBulkInitialResponseDto> syncBulkInitial(
            @Parameter(
                description = "러닝 데이터 초기 동기화 요청",
                required = true
            )
            @RequestBody SaveBulkInitialRequestDto request) {


        // request 검증
        validateBulkInitialRequest(request);

        log.info("러닝 데이터 대량 동기화 시작: userId={}, sessionCount={}", 
                request.getUserId(), 
                request.getRunningSessions() != null ? request.getRunningSessions().size() : 0);


        LocalDateTime syncStartTime = LocalDateTime.now();
        List<SaveBulkInitialResponseDto.SuccessfulSession> successfulSessions = new ArrayList<>();
        List<SaveBulkInitialResponseDto.FailedSession> failedSessions = new ArrayList<>();

        // 각 러닝 세션을 EndRunningSessionService를 통해 처리
        for (SaveBulkInitialRequestDto.RunningSessionData sessionData : request.getRunningSessions()) {
            try {
                // SaveBulkInitialRequestDto를 EndRunningSessionRequest로 변환
                EndRunningSessionRequest endRequest = convertToEndRunningSessionRequest(request.getUserId(), sessionData);
                
                // EndRunningSessionService를 통해 세션 저장
                RunningSession savedSession = endRunningSessionService.endRunningSession(endRequest);
                
                // 성공한 세션 정보 추가
                successfulSessions.add(createSuccessfulSession(sessionData, savedSession));
                
                log.debug("러닝 세션 저장 성공: workoutId={}, sessionId={}", 
                         sessionData.getWorkoutId(), savedSession.getId());
                
            } catch (Exception e) {
                // 실패한 세션 정보 추가
                failedSessions.add(createFailedSession(sessionData, e.getMessage()));
                
                log.error("러닝 세션 저장 실패: workoutId={}, error={}", 
                         sessionData.getWorkoutId(), e.getMessage(), e);
            }
        }

        LocalDateTime syncEndTime = LocalDateTime.now();
        long totalSyncTimeMs = java.time.Duration.between(syncStartTime, syncEndTime).toMillis();
        
        // 응답 데이터 생성
        SaveBulkInitialResponseDto response = SaveBulkInitialResponseDto.builder()
                .success(failedSessions.isEmpty())
                .message(failedSessions.isEmpty() ? 
                        "러닝 데이터 초기 동기화가 완료되었습니다." : 
                        String.format("러닝 데이터 동기화 완료: 성공 %d개, 실패 %d개", 
                                     successfulSessions.size(), failedSessions.size()))
                .timestamp(syncEndTime)
                .data(SaveBulkInitialResponseDto.SyncResultData.builder()
                        .userId(request.getUserId())
                        .syncStartTime(request.getSyncStartTime())
                        .syncEndTime(syncEndTime)
                        .totalSyncTimeMs(totalSyncTimeMs)
                        .totalSessionsProcessed(request.getRunningSessions().size())
                        .successfulSessionsCount(successfulSessions.size())
                        .failedSessionsCount(failedSessions.size())
                        .successRate(calculateSuccessRate(successfulSessions.size(), request.getRunningSessions().size()))
                        .dataSummary(createDataSummaryFromSuccessfulSessions(successfulSessions))
                        .successfulSessions(successfulSessions)
                        .failedSessions(failedSessions)
                        .build())
                .build();

        log.info("러닝 데이터 대량 동기화 완료: userId={}, 성공={}, 실패={}, 소요시간={}ms", 
                request.getUserId(), successfulSessions.size(), failedSessions.size(), totalSyncTimeMs);

        return ResponseEntity.ok(response);
    }

    /**
     * SaveBulkInitialRequestDto.RunningSessionData를 EndRunningSessionRequest로 변환
     */
    private EndRunningSessionRequest convertToEndRunningSessionRequest(Long userId, 
                                                                      SaveBulkInitialRequestDto.RunningSessionData sessionData) {
        
        // 경로 포인트 데이터 변환
        List<RoutePointData> routePoints = new ArrayList<>();
        if (sessionData.getRoute() != null) {
            for (SaveBulkInitialRequestDto.RoutePoint routePoint : sessionData.getRoute()) {
                routePoints.add(RoutePointData.builder()
                        .latitude(routePoint.getLatitude())
                        .longitude(routePoint.getLongitude())
                        .altitude(BigDecimal.ZERO) // 기본값 설정
                        .timestamp(routePoint.getTimestamp())
                        .speed(BigDecimal.ZERO) // 기본값 설정
                        .verticalAccuracy(BigDecimal.ZERO) // 기본값 설정
                        .horizontalAccuracy(BigDecimal.ZERO) // 기본값 설정
                        .course(BigDecimal.ZERO) // 기본값 설정
                        .build());
            }
        }

        return EndRunningSessionRequest.builder()
                .playerId(userId)
                .endTime(sessionData.getEndTime())
                .duration(sessionData.getDuration())
                .activeDuration(sessionData.getDuration()) // 기본값으로 duration 사용
                .distance(sessionData.getDistance())
                .totalCalories(sessionData.getCalories() != null ? BigDecimal.valueOf(sessionData.getCalories()) : BigDecimal.ZERO)
                .averagePace(BigDecimal.ZERO) // 기본값 설정
                .bestPace(BigDecimal.ZERO) // 기본값 설정
                .averageSpeed(BigDecimal.ZERO) // 기본값 설정
                .maxSpeed(BigDecimal.ZERO) // 기본값 설정
                .totalAscent(BigDecimal.ZERO) // 기본값 설정
                .totalDescent(BigDecimal.ZERO) // 기본값 설정
                .weatherTemperature(BigDecimal.ZERO) // 기본값 설정
                .weatherHumidity(0) // 기본값 설정
                .sourceDevice("iOS") // 기본값 설정
                .sourceApp("TurtleRun") // 기본값 설정
                .heartRateData(new ArrayList<>()) // 빈 리스트
                .routePoints(routePoints)
                .splits(new ArrayList<>()) // 빈 리스트
                .statistics(null) // null로 설정
                .build();
    }

    /**
     * 성공한 세션 정보 생성
     */
    private SaveBulkInitialResponseDto.SuccessfulSession createSuccessfulSession(
            SaveBulkInitialRequestDto.RunningSessionData sessionData, 
            RunningSession savedSession) {
        
        return SaveBulkInitialResponseDto.SuccessfulSession.builder()
                .workoutId(sessionData.getWorkoutId())
                .serverSessionId(savedSession.getId())
                .syncedAt(LocalDateTime.now())
                .dataCount(SaveBulkInitialResponseDto.DataSummary.builder()
                        .runningSessions(1)
                        .routePoints(sessionData.getRoute() != null ? sessionData.getRoute().size() : 0)
                        .totalDistance(sessionData.getDistance() != null ? sessionData.getDistance() : BigDecimal.ZERO)
                        .totalDuration(sessionData.getDuration() != null ? sessionData.getDuration() : 0)
                        .totalCalories(sessionData.getCalories() != null ? sessionData.getCalories() : 0)
                        .build())
                .build();
    }

    /**
     * 실패한 세션 정보 생성
     */
    private SaveBulkInitialResponseDto.FailedSession createFailedSession(
            SaveBulkInitialRequestDto.RunningSessionData sessionData, 
            String errorMessage) {
        
        return SaveBulkInitialResponseDto.FailedSession.builder()
                .workoutId(sessionData.getWorkoutId())
                .errorMessage(errorMessage)
                .failedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 성공률 계산
     */
    private double calculateSuccessRate(int successCount, int totalCount) {
        if (totalCount == 0) return 0.0;
        return (double) successCount / totalCount * 100.0;
    }

    /**
     * 성공한 세션들로부터 데이터 요약 생성
     */
    private SaveBulkInitialResponseDto.DataSummary createDataSummaryFromSuccessfulSessions(
            List<SaveBulkInitialResponseDto.SuccessfulSession> successfulSessions) {
        
        int totalRoutePoints = 0;
        BigDecimal totalDistance = BigDecimal.ZERO;
        int totalDuration = 0;
        int totalCalories = 0;

        for (SaveBulkInitialResponseDto.SuccessfulSession session : successfulSessions) {
            SaveBulkInitialResponseDto.DataSummary dataCount = session.getDataCount();
            if (dataCount != null) {
                totalRoutePoints += dataCount.getRoutePoints();
                totalDistance = totalDistance.add(dataCount.getTotalDistance());
                totalDuration += dataCount.getTotalDuration();
                totalCalories += dataCount.getTotalCalories();
            }
        }

        return SaveBulkInitialResponseDto.DataSummary.builder()
                .runningSessions(successfulSessions.size())
                .routePoints(totalRoutePoints)
                .totalDistance(totalDistance)
                .totalDuration(totalDuration)
                .totalCalories(totalCalories)
                .build();
    }

    /**
     * 러닝 데이터 초기 동기화 요청 데이터 검증
     */
    private void validateBulkInitialRequest(SaveBulkInitialRequestDto request) throws BulkInitialException{
        if (request == null) {
            throw BusinessException.badRequest("요청 데이터가 null입니다");
        }

        if (request.getUserId() == null) {
            throw BusinessException.badRequest("userId는 필수입니다");
        }

        if (request.getSyncStartTime() == null) {
            throw BusinessException.badRequest("syncStartTime은 필수입니다");
        }

        if (request.getRunningSessions() == null || request.getRunningSessions().isEmpty()) {
            throw BusinessException.badRequest("runningSessions는 비어있을 수 없습니다");
        }

        // 동기화 기간 검증 (30일 이내)
        LocalDateTime now = LocalDateTime.now();
        if (request.getSyncStartTime().isBefore(now.minusDays(30))) {
            throw BusinessException.badRequest("동기화 시작 시간은 현재 시점으로부터 30일 이내여야 합니다");
        }

        // 데이터 크기 검증
        int totalSessions = request.getRunningSessions().size();
        if (totalSessions > 100) {
            throw BusinessException.badRequest("동기화할 세션 수가 너무 많습니다. 최대 100개까지 허용됩니다.");
        }
    }
}