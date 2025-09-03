package com.turtleRun.be.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정
 * @CreatedDate, @LastModifiedDate 어노테이션을 활성화합니다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
