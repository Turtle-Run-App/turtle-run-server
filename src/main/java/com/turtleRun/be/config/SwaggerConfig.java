package com.turtleRun.be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger 3 (OpenAPI 3) 설정 클래스
 * API 문서화 및 테스트 환경을 구성
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Turtle Run HealthKit API")
                        .version("1.0.0")
                        .description("""
                            🐢 Turtle Run 애플리케이션의 HealthKit 데이터 동기화 및 운동 세션 관리 API
                            
                            ## 주요 기능
                            - **HealthKit 데이터 동기화**: 클라이언트에서 HealthKit 데이터를 서버로 전송
                            - **운동 세션 관리**: GPS 기반 운동 세션 생성 및 분석
                            - **데이터 조회**: 사용자별 HealthKit 데이터 및 통계 조회
                            
                            ## API 그룹
                            - `/api/healthkit/sync` - HealthKit 동기화
                            - `/api/healthkit/data` - HealthKit 데이터 관리
                            - `/api/workout/session` - 운동 세션 관리
                            """)
                        .contact(new Contact()
                                .name("Turtle Run Development Team")
                                .email("dev@turtlerun.com")
                                .url("https://turtlerun.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("로컬 개발 환경"),
                        new Server()
                                .url("https://api.turtlerun.com")
                                .description("프로덕션 환경")
                ));
    }
} 