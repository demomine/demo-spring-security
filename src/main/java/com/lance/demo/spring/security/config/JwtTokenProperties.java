package com.lance.demo.spring.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt.token")
@Component
@Data
public class JwtTokenProperties {
    private int expire;
    private String key;
    private String issuer = "lance";
    private String subject;
    private String audience  = "app";
    private String notBefore;
}
