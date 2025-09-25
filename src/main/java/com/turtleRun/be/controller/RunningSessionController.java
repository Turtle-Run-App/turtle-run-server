package com.turtleRun.be.controller;

import com.turtleRun.be.common.model.SaveRunningSessionRequestDto;
import com.turtleRun.be.common.model.SaveRunningSessionResponseDto;
import com.turtleRun.be.running.application.service.RunningApplicationService;
import com.turtleRun.be.running.exception.RunningException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/runningSession")
@Tag(name = "Running Session", description = "러닝 세션 관리 API")
public class RunningSessionController {

    private final RunningApplicationService runningApplicationService;

    @Autowired
    public RunningSessionController(RunningApplicationService runningApplicationService) {
        this.runningApplicationService = runningApplicationService;
    }

    @GetMapping("healthCheck")
    @Operation(
        summary = "헬스 체크",
        description = "RunningSession 컨트롤러 상태 확인"
    )
    public String runningSessionHealthCheck(
            @Parameter(description = "테스트 플래그", example = "test")
            @RequestParam String testFlag) {
        return "runningSession is ok with : " + testFlag;
    }

    @PostMapping
    @Operation(
        summary = "러닝 세션 저장",
        description = "HealthKit에서 받은 러닝 데이터를 저장합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "러닝 세션 저장 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SaveRunningSessionResponseDto.class),
                examples = @ExampleObject(
                    name = "성공 응답",
                    value = """
                        {
                          "success": true,
                          "message": "러닝 세션이 성공적으로 저장되었습니다.",
                          "timestamp": "2024-01-15T10:00:00Z",
                          "data": {
                            "sessionId": 123,
                            "workoutId": "550e8400-e29b-41d4-a716-446655440000",
                            "startTime": "2024-01-15T09:00:00Z",
                            "endTime": "2024-01-15T10:00:00Z",
                            "workoutType": "running",
                            "duration": 3600,
                            "distance": 5000.0,
                            "calories": 300,
                            "avgHeartRate": 140,
                            "dataSummary": {
                              "routePointCount": 50,
                              "totalDistance": 5000.0,
                              "totalDuration": 3600,
                              "totalCalories": 300
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
                schema = @Schema(implementation = SaveRunningSessionResponseDto.class)
            )
        )
    })
    public ResponseEntity<SaveRunningSessionResponseDto> saveRunningSession(
            @Parameter(
                description = "러닝 세션 저장 요청 데이터",
                required = true
            )
            @RequestBody SaveRunningSessionRequestDto request) {

        // 입력 데이터 검증
        validateRunningSessionRequest(request);

        // TODO: RunningApplicationService를 통해 실제 저장 로직 구현
        // RunningSession savedSession = runningApplicationService.saveRunningSession(request);

        // 임시 응답 데이터 생성
        SaveRunningSessionResponseDto response = SaveRunningSessionResponseDto.builder()
                .success(true)
                .message("러닝 세션이 성공적으로 저장되었습니다.")
                .timestamp(LocalDateTime.now())
                .data(SaveRunningSessionResponseDto.RunningSessionData.builder()
                        .sessionId(123L) // 임시 ID
                        .workoutId(request.getWorkoutId())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .workoutType(request.getWorkoutType())
                        .duration(request.getDuration())
                        .distance(request.getDistance())
                        .calories(request.getCalories())
                        .avgHeartRate(request.getAvgHeartRate())
                        .createdAt(LocalDateTime.now())
                        .dataSummary(SaveRunningSessionResponseDto.DataSummary.builder()
                                .routePointCount(request.getRoute() != null ? request.getRoute().size() : 0)
                                .totalDistance(request.getDistance() != null ? request.getDistance() : BigDecimal.ZERO)
                                .totalDuration(request.getDuration() != null ? request.getDuration() : 0)
                                .totalCalories(request.getCalories() != null ? request.getCalories() : 0)
                                .build())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 러닝 세션 요청 데이터 검증
     */
    private void validateRunningSessionRequest(SaveRunningSessionRequestDto request) {
        if (request == null) {
            throw new RunningException.SessionInvalidData("요청 데이터가 null입니다");
        }

        if (request.getWorkoutId() == null || request.getWorkoutId().trim().isEmpty()) {
            throw new RunningException.SessionInvalidData("workoutId는 필수입니다");
        }

        if (request.getStartTime() == null) {
            throw new RunningException.SessionInvalidData("startTime은 필수입니다");
        }

        if (request.getEndTime() == null) {
            throw new RunningException.SessionInvalidData("endTime은 필수입니다");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RunningException.SessionInvalidData("startTime은 endTime보다 이전이어야 합니다");
        }

        if (request.getDuration() != null && request.getDuration() <= 0) {
            throw new RunningException.DurationInvalid(request.getDuration(), "duration은 0보다 커야 합니다");
        }

        if (request.getDistance() != null && request.getDistance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RunningException.DistanceInvalid(request.getDistance(), "distance는 0보다 커야 합니다");
        }

        if (request.getCalories() != null && request.getCalories() < 0) {
            throw new RunningException.CaloriesInvalid(request.getCalories(), "calories는 0 이상이어야 합니다");
        }

        if (request.getAvgHeartRate() != null && (request.getAvgHeartRate() < 0 || request.getAvgHeartRate() > 300)) {
            throw new RunningException.HeartRateInvalid(request.getAvgHeartRate(), "avgHeartRate는 0~300 범위여야 합니다");
        }
    }
}
