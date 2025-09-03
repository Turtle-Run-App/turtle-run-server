package com.turtleRun.be.auth.presentation.controller;

import com.turtleRun.be.auth.application.dto.LoginRequestDto;
import com.turtleRun.be.auth.application.dto.LoginResponseDto;
import com.turtleRun.be.auth.application.dto.SignUpRequestDto;
import com.turtleRun.be.auth.application.dto.SignUpResponseDto;
import com.turtleRun.be.auth.application.service.AuthApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 컨트롤러
 * 프레젠테이션 레이어에서 HTTP 요청을 처리하고 애플리케이션 서비스를 호출합니다.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthApplicationService authApplicationService;
    
    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }
    
    /**
     * 사용자 회원가입
     * 
     * @param request 회원가입 요청 정보
     * @return 회원가입 결과
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 사용자명"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto request) {
        logger.info("회원가입 요청: {}", request.getEmail());
        
        SignUpResponseDto response = authApplicationService.signUp(request);
        
        if (response.isSuccess()) {
            logger.info("회원가입 성공: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            logger.warn("회원가입 실패: {} - {}", request.getEmail(), response.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 사용자 로그인
     * 
     * @param request 로그인 요청 정보
     * @return 로그인 결과 (JWT 토큰 포함)
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증을 수행하고 JWT 토큰을 발급합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        logger.info("로그인 요청: {}", request.getUsernameOrEmail());
        
        LoginResponseDto response = authApplicationService.login(request);
        
        if (response.isSuccess()) {
            logger.info("로그인 성공: {}", request.getUsernameOrEmail());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("로그인 실패: {} - {}", request.getUsernameOrEmail(), response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * 사용자 정보 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SignUpResponseDto> getUserInfo(@PathVariable String userId) {
        logger.info("사용자 정보 조회 요청: {}", userId);
        
        SignUpResponseDto response = authApplicationService.getUserInfo(userId);
        
        if (response.isSuccess()) {
            logger.info("사용자 정보 조회 성공: {}", userId);
            return ResponseEntity.ok(response);
        } else {
            logger.warn("사용자 정보 조회 실패: {} - {}", userId, response.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * 현재 사용자 정보 조회 (JWT 토큰 기반)
     * 
     * @param authorization Authorization 헤더 (Bearer 토큰)
     * @return 현재 사용자 정보
     */
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "JWT 토큰을 통해 현재 로그인한 사용자의 정보를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SignUpResponseDto> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        logger.info("현재 사용자 정보 조회 요청");
        
        // Authorization 헤더에서 Bearer 토큰 추출
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.warn("잘못된 Authorization 헤더");
            SignUpResponseDto response = SignUpResponseDto.failure("인증 토큰이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        String token = authorization.substring(7); // "Bearer " 제거
        
        try {
            // JWT 토큰에서 사용자 ID 추출 (실제로는 JwtTokenService를 주입받아 사용)
            // 여기서는 간단히 예시로 처리
            SignUpResponseDto response = SignUpResponseDto.failure("JWT 토큰 처리 기능은 5단계에서 구현됩니다");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
            
        } catch (Exception e) {
            logger.error("현재 사용자 정보 조회 중 오류 발생", e);
            SignUpResponseDto response = SignUpResponseDto.failure("사용자 정보 조회 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
