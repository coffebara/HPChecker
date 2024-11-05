package com.solomonm.HPChecker.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 서버 config
 */
@Getter
@Configuration
public class ServerConfig {
    @Value("${server.host}")
    private String host;
    @Value("${server.user}")
    private String user; //ID
    @Value("${server.password}")
    private String password;
    @Value("${server.port}")
    private int port; // 기본 SSH 포트
}
