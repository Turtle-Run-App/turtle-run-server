package com.turtleRun.be.controller;

import com.turtleRun.be.healthkit.exception.HealthKitException;
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

import com.turtleRun.be.common.model.HealthKitSyncRequestDto;
import com.turtleRun.be.common.model.HealthKitSyncResponseDto;

/**
 * HealthKit 데이터 동기화를 위한 Controller
 * 클라이언트에서 HealthKit 데이터를 서버로 전송하여 동기화
 */
@RestController
@RequestMapping("api/healthkit/sync")
@Tag(name = "HealthKit 동기화", description = "HealthKit 데이터 동기화 관련 API")
public class HealthKitSyncController {

    // TODO: HealthKit 동기화 서비스 주입 필요
    // @Autowired
    // private HealthKitSyncService healthKitSyncService;

    /**
     * HealthKit 데이터 동기화
     * 클라이언트에서 HealthKit 데이터를 서버로 전송
     */
    @Operation(
        summary = "HealthKit 데이터 동기화",
        description = "클라이언트에서 HealthKit 데이터를 서버로 전송하여 동기화합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "동기화 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HealthKitSyncResponseDto.class),
                examples = @ExampleObject(
                    name = "성공 응답",
                    value = """
                        {
                          "success": true,
                          "message": "HealthKit 데이터 동기화가 완료되었습니다.",
                          "data": {
                            "syncedDataCount": 150,
                            "syncSessionId": "sync_12345"
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
                schema = @Schema(implementation = HealthKitSyncResponseDto.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<HealthKitSyncResponseDto> syncHealthKitData(
            @Parameter(
                description = "HealthKit 데이터",
                required = true,
                example = """
                    {
                      "userId": "user123",
                      "syncStartTime": "2024-01-15T09:00:00Z",
                      "syncEndTime": "2024-01-15T10:00:00Z",
                      "healthKitDataList": [
                        {
                          "dataType": "steps",
                          "value": 5000,
                          "unit": "count",
                          "startTime": "2024-01-15T09:00:00Z",
                          "endTime": "2024-01-15T10:00:00Z"
                        }
                      ]
                    }
                    """
            )
            @RequestBody HealthKitSyncRequestDto healthKitData) {
        
        // 입력 데이터 검증
        validateHealthKitSyncRequest(healthKitData);
        
        // TODO: HealthKit 데이터 동기화 로직 구현
        // HealthKitSyncResponse response = healthKitSyncService.syncHealthKitData(healthKitData);
        
        HealthKitSyncResponseDto response = HealthKitSyncResponseDto.builder()
                .success(true)
                .message("HealthKit 데이터 동기화가 완료되었습니다.")
                .timestamp(java.time.LocalDateTime.now())
                .data(HealthKitSyncResponseDto.SyncStatusData.builder()
                        .userId(Long.valueOf(healthKitData.getUserId().replace("user", "")))
                        .syncStatus("COMPLETED")
                        .lastSyncTime(java.time.LocalDateTime.now())
                        .progressPercentage(100)
                        .syncedDataCount(healthKitData.getHealthKitDataList() != null ? healthKitData.getHealthKitDataList().size() : 0)
                        .totalDataCount(healthKitData.getHealthKitDataList() != null ? healthKitData.getHealthKitDataList().size() : 0)
                        .build())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * 동기화 상태 확인
     * 특정 사용자의 HealthKit 동기화 상태를 확인
     */
    @Operation(
        summary = "동기화 상태 확인",
        description = "특정 사용자의 HealthKit 동기화 상태를 확인합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "상태 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HealthKitSyncResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        )
    })
    @GetMapping("status/{userId}")
    public ResponseEntity<HealthKitSyncResponseDto> getSyncStatus(
            @Parameter(description = "사용자 ID", example = "user123")
            @PathVariable String userId) {
        
        // 사용자 ID 검증
        validateUserId(userId);
        
        // TODO: 동기화 상태 조회 로직 구현
        // HealthKitSyncResponseDto syncStatus = healthKitSyncService.getSyncStatus(userId);
        
        HealthKitSyncResponseDto response = HealthKitSyncResponseDto.builder()
                .success(true)
                .message("동기화 상태를 성공적으로 조회했습니다.")
                .timestamp(java.time.LocalDateTime.now())
                .data(HealthKitSyncResponseDto.SyncStatusData.builder()
                        .userId(Long.valueOf(userId.replace("user", "")))
                        .syncStatus("UNKNOWN")
                        .lastSyncTime(null)
                        .progressPercentage(0)
                        .syncedDataCount(0)
                        .totalDataCount(0)
                        .build())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * 동기화 히스토리 조회
     * 특정 사용자의 HealthKit 동기화 히스토리를 조회
     */
    @Operation(
        summary = "동기화 히스토리 조회",
        description = "특정 사용자의 HealthKit 동기화 히스토리를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "히스토리 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HealthKitSyncResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        )
    })
    @GetMapping("history/{userId}")
    public ResponseEntity<HealthKitSyncResponseDto> getSyncHistory(
            @Parameter(description = "사용자 ID", example = "user123")
            @PathVariable String userId,
            @Parameter(description = "조회할 히스토리 개수", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        
        // 사용자 ID 검증
        validateUserId(userId);
        
        // limit 검증
        if (limit <= 0 || limit > 100) {
            throw new HealthKitException.InvalidData("limit은 1~100 범위여야 합니다", "limit", String.valueOf(limit));
        }
        
        // TODO: 동기화 히스토리 조회 로직 구현
        // HealthKitSyncResponseDto history = healthKitSyncService.getSyncHistory(userId, limit);
        
        HealthKitSyncResponseDto response = HealthKitSyncResponseDto.builder()
                .success(true)
                .message("동기화 히스토리를 성공적으로 조회했습니다.")
                .timestamp(java.time.LocalDateTime.now())
                .data(HealthKitSyncResponseDto.SyncHistoryData.builder()
                        .userId(Long.valueOf(userId.replace("user", "")))
                        .history(new java.util.ArrayList<>())
                        .totalCount(0)
                        .build())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * HealthKit 동기화 요청 데이터 검증
     */
    private void validateHealthKitSyncRequest(HealthKitSyncRequestDto healthKitData) {
        if (healthKitData == null) {
            throw new HealthKitException.InvalidData("HealthKit 데이터가 null입니다");
        }

        if (healthKitData.getUserId() == null || healthKitData.getUserId().trim().isEmpty()) {
            throw new HealthKitException.InvalidData("userId는 필수입니다");
        }

        if (healthKitData.getSyncStartTime() == null) {
            throw new HealthKitException.InvalidData("syncStartTime은 필수입니다");
        }

        if (healthKitData.getSyncEndTime() == null) {
            throw new HealthKitException.InvalidData("syncEndTime은 필수입니다");
        }

        if (healthKitData.getSyncStartTime().isAfter(healthKitData.getSyncEndTime())) {
            throw new HealthKitException.InvalidData("syncStartTime은 syncEndTime보다 이전이어야 합니다");
        }

        if (healthKitData.getHealthKitDataList() == null || healthKitData.getHealthKitDataList().isEmpty()) {
            throw new HealthKitException.InvalidData("healthKitDataList는 비어있을 수 없습니다");
        }
    }

    /**
     * 사용자 ID 검증
     */
    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new HealthKitException.InvalidData("사용자 ID는 필수입니다");
        }

        if (!userId.startsWith("user")) {
            throw new HealthKitException.InvalidData("사용자 ID 형식이 올바르지 않습니다", "userId", userId);
        }
    }
} 