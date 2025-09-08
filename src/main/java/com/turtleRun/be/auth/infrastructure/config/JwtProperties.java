package com.turtleRun.be.auth.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 설정 Properties
 * application.yml에서 JWT 관련 설정을 읽어옵니다.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    private String secret;
    private long expiration;
    
    public JwtProperties() {
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public long getExpiration() {
        return expiration;
    }
    
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
