package com.turtleRun.be.controller;

import com.turtleRun.be.common.model.SaveRunningSessionRequestDto;
import com.turtleRun.be.common.model.SaveRunningSessionResponseDto;
import com.turtleRun.be.running.application.service.RunningApplicationService;
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

        try {
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

        } catch (Exception e) {
            SaveRunningSessionResponseDto errorResponse = SaveRunningSessionResponseDto.builder()
                    .success(false)
                    .message("러닝 세션 저장 중 오류가 발생했습니다: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
