package com.turtleRun.be.controller;

import com.turtleRun.be.common.exception.BulkInitialException;
import com.turtleRun.be.common.model.SaveBulkInitialRequestDto;
import com.turtleRun.be.common.model.SaveBulkInitialResponseDto;
import com.turtleRun.be.running.application.service.EndRunningSessionService;
import com.turtleRun.be.running.application.dto.EndRunningSessionRequest;
import com.turtleRun.be.running.domain.entity.RunningSession;
import com.turtleRun.be.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BulkInitialController 테스트 클래스
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BulkInitialController 테스트")
class BulkInitialControllerTest {

    @Mock
    private EndRunningSessionService endRunningSessionService;

    @InjectMocks
    private BulkInitialController bulkInitialController;

    private SaveBulkInitialRequestDto validRequest;
    private RunningSession mockRunningSession;

    @BeforeEach
    void setUp() {
        // 유효한 요청 데이터 설정
        validRequest = createValidRequest();
        
        // Mock RunningSession 설정
        mockRunningSession = createMockRunningSession();
    }

    @Test
    @DisplayName("syncBulkInitial - 성공 케이스")
    void syncBulkInitial_Success() {
        // Given
        when(endRunningSessionService.endRunningSession(any(EndRunningSessionRequest.class)))
                .thenReturn(mockRunningSession);

        // When
        ResponseEntity<SaveBulkInitialResponseDto> response = bulkInitialController.syncBulkInitial(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).contains("러닝 데이터 초기 동기화가 완료되었습니다.");
        assertThat(response.getBody().getData().getSuccessfulSessionsCount()).isEqualTo(2);
        assertThat(response.getBody().getData().getFailedSessionsCount()).isEqualTo(0);
        assertThat(response.getBody().getData().getSuccessRate()).isEqualTo(100.0);

        // EndRunningSessionService가 2번 호출되었는지 확인
        verify(endRunningSessionService, times(2)).endRunningSession(any(EndRunningSessionRequest.class));
    }

    @Test
    @DisplayName("syncBulkInitial - 일부 실패 케이스")
    void syncBulkInitial_PartialFailure() {
        // Given
        when(endRunningSessionService.endRunningSession(any(EndRunningSessionRequest.class)))
                .thenReturn(mockRunningSession)
                .thenThrow(new RuntimeException("데이터베이스 오류"));

        // When
        ResponseEntity<SaveBulkInitialResponseDto> response = bulkInitialController.syncBulkInitial(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("러닝 데이터 동기화 완료: 성공 1개, 실패 1개");
        assertThat(response.getBody().getData().getSuccessfulSessionsCount()).isEqualTo(1);
        assertThat(response.getBody().getData().getFailedSessionsCount()).isEqualTo(1);
        assertThat(response.getBody().getData().getSuccessRate()).isEqualTo(50.0);

        // 실패한 세션의 에러 메시지 확인
        assertThat(response.getBody().getData().getFailedSessions()).hasSize(1);
        assertThat(response.getBody().getData().getFailedSessions().get(0).getErrorMessage())
                .contains("데이터베이스 오류");
    }

    @Test
    @DisplayName("syncBulkInitial - 전체 실패 케이스")
    void syncBulkInitial_AllFailure() {
        // Given
        when(endRunningSessionService.endRunningSession(any(EndRunningSessionRequest.class)))
                .thenThrow(new RuntimeException("서비스 오류"));

        // When
        ResponseEntity<SaveBulkInitialResponseDto> response = bulkInitialController.syncBulkInitial(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("러닝 데이터 동기화 완료: 성공 0개, 실패 2개");
        assertThat(response.getBody().getData().getSuccessfulSessionsCount()).isEqualTo(0);
        assertThat(response.getBody().getData().getFailedSessionsCount()).isEqualTo(2);
        assertThat(response.getBody().getData().getSuccessRate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (null request)")
    void syncBulkInitial_InvalidRequest_Null() {
        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(null))
                .isInstanceOf(BulkInitialException.InvalidData.class)
                .hasMessage("요청 데이터가 null입니다");
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (null userId)")
    void syncBulkInitial_InvalidRequest_NullUserId() {
        // Given
        validRequest.setUserId(null);

        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(validRequest))
                .isInstanceOf(BulkInitialException.InvalidData.class)
                .hasMessage("userId는 필수입니다");
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (null syncStartTime)")
    void syncBulkInitial_InvalidRequest_NullSyncStartTime() {
        // Given
        validRequest.setSyncStartTime(null);

        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(validRequest))
                .isInstanceOf(BulkInitialException.InvalidData.class)
                .hasMessage("syncStartTime은 필수입니다");
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (빈 runningSessions)")
    void syncBulkInitial_InvalidRequest_EmptyRunningSessions() {
        // Given
        validRequest.setRunningSessions(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(validRequest))
                .isInstanceOf(BulkInitialException.InvalidData.class)
                .hasMessage("runningSessions는 비어있을 수 없습니다");
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (30일 초과 syncStartTime)")
    void syncBulkInitial_InvalidRequest_InvalidSyncPeriod() {
        // Given
        validRequest.setSyncStartTime(LocalDateTime.now().minusDays(31));

        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(validRequest))
                .isInstanceOf(BulkInitialException.InvalidSyncPeriod.class);
    }

    @Test
    @DisplayName("syncBulkInitial - 잘못된 요청 (100개 초과 세션)")
    void syncBulkInitial_InvalidRequest_TooManySessions() {
        // Given
        List<SaveBulkInitialRequestDto.RunningSessionData> manySessions = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            manySessions.add(createRunningSessionData("workout-" + i));
        }
        validRequest.setRunningSessions(manySessions);

        // When & Then
        assertThatThrownBy(() -> bulkInitialController.syncBulkInitial(validRequest))
                .isInstanceOf(BulkInitialException.DataTooLarge.class);
    }

    @Test
    @DisplayName("syncBulkInitial - 데이터 요약 생성 확인")
    void syncBulkInitial_DataSummary() {
        // Given
        when(endRunningSessionService.endRunningSession(any(EndRunningSessionRequest.class)))
                .thenReturn(mockRunningSession);

        // When
        ResponseEntity<SaveBulkInitialResponseDto> response = bulkInitialController.syncBulkInitial(validRequest);

        // Then
        SaveBulkInitialResponseDto.DataSummary dataSummary = response.getBody().getData().getDataSummary();
        assertThat(dataSummary.getRunningSessions()).isEqualTo(2);
        assertThat(dataSummary.getRoutePoints()).isEqualTo(4); // 2개 세션 * 2개 경로포인트
        assertThat(dataSummary.getTotalDistance()).isEqualByComparingTo(BigDecimal.valueOf(10000)); // 5000 * 2
        assertThat(dataSummary.getTotalDuration()).isEqualTo(3600); // 1800 * 2
        assertThat(dataSummary.getTotalCalories()).isEqualTo(700); // 350 * 2
    }

    /**
     * 유효한 요청 데이터 생성
     */
    private SaveBulkInitialRequestDto createValidRequest() {
        List<SaveBulkInitialRequestDto.RunningSessionData> sessions = new ArrayList<>();
        sessions.add(createRunningSessionData("workout-1"));
        sessions.add(createRunningSessionData("workout-2"));

        return SaveBulkInitialRequestDto.builder()
                .userId(1001L)
                .syncStartTime(LocalDateTime.now().minusDays(10))
                .syncEndTime(LocalDateTime.now())
                .runningSessions(sessions)
                .build();
    }

    /**
     * 러닝 세션 데이터 생성
     */
    private SaveBulkInitialRequestDto.RunningSessionData createRunningSessionData(String workoutId) {
        List<SaveBulkInitialRequestDto.RoutePoint> routePoints = new ArrayList<>();
        routePoints.add(SaveBulkInitialRequestDto.RoutePoint.builder()
                .latitude(BigDecimal.valueOf(37.5665))
                .longitude(BigDecimal.valueOf(126.9780))
                .timestamp(LocalDateTime.now().minusMinutes(30))
                .build());
        routePoints.add(SaveBulkInitialRequestDto.RoutePoint.builder()
                .latitude(BigDecimal.valueOf(37.5666))
                .longitude(BigDecimal.valueOf(126.9781))
                .timestamp(LocalDateTime.now())
                .build());

        return SaveBulkInitialRequestDto.RunningSessionData.builder()
                .workoutId(workoutId)
                .startTime(LocalDateTime.now().minusMinutes(30))
                .endTime(LocalDateTime.now())
                .workoutType("running")
                .distance(BigDecimal.valueOf(5000))
                .duration(1800)
                .calories(350)
                .avgHeartRate(150)
                .route(routePoints)
                .build();
    }

    /**
     * Mock RunningSession 생성
     */
    private RunningSession createMockRunningSession() {
        RunningSession session = new RunningSession();
        session.setId(12345L);
        
        Player player = new Player();
        player.setPlayerId(1001L);
        session.setPlayer(player);
        
        session.setStartTime(LocalDateTime.now().minusMinutes(30));
        session.setEndTime(LocalDateTime.now());
        session.setDuration(1800);
        session.setActiveDuration(1800);
        session.setDistance(BigDecimal.valueOf(5000));
        session.setTotalCalories(BigDecimal.valueOf(350));
        session.setAveragePace(BigDecimal.valueOf(6.0));
        session.setBestPace(BigDecimal.valueOf(5.5));
        session.setAverageSpeed(BigDecimal.valueOf(10.0));
        session.setMaxSpeed(BigDecimal.valueOf(12.0));
        session.setTotalAscent(BigDecimal.valueOf(50));
        session.setTotalDescent(BigDecimal.valueOf(45));
        session.setWeatherTemperature(BigDecimal.valueOf(20.0));
        session.setWeatherHumidity(60);
        session.setSourceDevice("iOS");
        session.setSourceApp("TurtleRun");
        
        return session;
    }
} 