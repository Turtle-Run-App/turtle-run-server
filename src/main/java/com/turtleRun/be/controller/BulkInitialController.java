package com.turtleRun.be.controller;

import com.turtleRun.be.common.model.SaveBulkInitialRequestDto;
import com.turtleRun.be.common.model.SaveBulkInitialResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RestController
@RequestMapping("api/bulkInitial")
@Tag(name = "Bulk Initial Sync", description = "신규 사용자 러닝 데이터 초기 동기화 API")
public class BulkInitialController {

    // TODO: BulkInitial 서비스 주입 필요
    // @Autowired
    // private BulkInitialService bulkInitialService;

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
                schema = @Schema(implementation = SaveBulkInitialResponseDto.class),
                examples = @ExampleObject(
                    name = "성공 응답",
                    value = """
                        {
                          "success": true,
                          "message": "러닝 데이터 초기 동기화가 완료되었습니다.",
                          "timestamp": "2024-01-15T10:00:00Z",
                          "data": {
                            "userId": 123,
                            "totalSessionsProcessed": 30,
                            "successfulSessionsCount": 28,
                            "failedSessionsCount": 2,
                            "successRate": 93.3,
                            "dataSummary": {
                              "runningSessions": 28,
                              "routePoints": 14000,
                              "totalDistance": 150000.0,
                              "totalDuration": 72000,
                              "totalCalories": 9000
                            }
                          }
                        }
                        """
                )
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

        try {
            // TODO: BulkInitial 서비스를 통해 실제 동기화 로직 구현
            // SaveBulkInitialResponseDto response = bulkInitialService.syncBulkInitial(request);

            // 임시 응답 데이터 생성
            LocalDateTime now = LocalDateTime.now();
            long syncTimeMs = 2500L; // 임시 동기화 시간
            
            SaveBulkInitialResponseDto response = SaveBulkInitialResponseDto.builder()
                    .success(true)
                    .message("러닝 데이터 초기 동기화가 완료되었습니다.")
                    .timestamp(now)
                    .data(SaveBulkInitialResponseDto.SyncResultData.builder()
                            .userId(request.getUserId())
                            .syncStartTime(request.getSyncStartTime())
                            .syncEndTime(now)
                            .totalSyncTimeMs(syncTimeMs)
                            .totalSessionsProcessed(request.getRunningSessions() != null ? request.getRunningSessions().size() : 0)
                            .successfulSessionsCount(request.getRunningSessions() != null ? request.getRunningSessions().size() : 0)
                            .failedSessionsCount(0)
                            .successRate(100.0)
                            .dataSummary(createDataSummary(request))
                            .successfulSessions(createSuccessfulSessionsList(request))
                            .failedSessions(new ArrayList<>())
                            .build())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            SaveBulkInitialResponseDto errorResponse = SaveBulkInitialResponseDto.builder()
                    .success(false)
                    .message("러닝 데이터 초기 동기화 중 오류가 발생했습니다: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 동기화된 데이터 요약 생성 (임시)
     */
    private SaveBulkInitialResponseDto.DataSummary createDataSummary(SaveBulkInitialRequestDto request) {
        if (request.getRunningSessions() == null || request.getRunningSessions().isEmpty()) {
            return SaveBulkInitialResponseDto.DataSummary.builder()
                    .runningSessions(0)
                    .routePoints(0)
                    .totalDistance(BigDecimal.ZERO)
                    .totalDuration(0)
                    .totalCalories(0)
                    .build();
        }

        int totalRoutePoints = 0;
        BigDecimal totalDistance = BigDecimal.ZERO;
        int totalDuration = 0;
        int totalCalories = 0;

        for (SaveBulkInitialRequestDto.RunningSessionData session : request.getRunningSessions()) {
            if (session.getRoute() != null) {
                totalRoutePoints += session.getRoute().size();
            }
            if (session.getDistance() != null) {
                totalDistance = totalDistance.add(session.getDistance());
            }
            if (session.getDuration() != null) {
                totalDuration += session.getDuration();
            }
            if (session.getCalories() != null) {
                totalCalories += session.getCalories();
            }
        }

        return SaveBulkInitialResponseDto.DataSummary.builder()
                .runningSessions(request.getRunningSessions().size())
                .routePoints(totalRoutePoints)
                .totalDistance(totalDistance)
                .totalDuration(totalDuration)
                .totalCalories(totalCalories)
                .build();
    }

    /**
     * 성공한 세션 목록 생성 (임시)
     */
    private List<SaveBulkInitialResponseDto.SuccessfulSession> createSuccessfulSessionsList(
            SaveBulkInitialRequestDto request) {
        
        List<SaveBulkInitialResponseDto.SuccessfulSession> successfulSessions = new ArrayList<>();
        
        if (request.getRunningSessions() != null) {
            for (int i = 0; i < request.getRunningSessions().size(); i++) {
                SaveBulkInitialRequestDto.RunningSessionData sessionData = request.getRunningSessions().get(i);
                
                SaveBulkInitialResponseDto.SuccessfulSession session = SaveBulkInitialResponseDto.SuccessfulSession.builder()
                        .workoutId(sessionData.getWorkoutId())
                        .serverSessionId(1000L + i) // 임시 서버 세션 ID
                        .syncedAt(LocalDateTime.now())
                        .dataCount(SaveBulkInitialResponseDto.DataSummary.builder()
                                .runningSessions(1)
                                .routePoints(sessionData.getRoute() != null ? sessionData.getRoute().size() : 0)
                                .totalDistance(sessionData.getDistance() != null ? sessionData.getDistance() : BigDecimal.ZERO)
                                .totalDuration(sessionData.getDuration() != null ? sessionData.getDuration() : 0)
                                .totalCalories(sessionData.getCalories() != null ? sessionData.getCalories() : 0)
                                .build())
                        .build();
                
                successfulSessions.add(session);
            }
        }
        
        return successfulSessions;
    }
}
