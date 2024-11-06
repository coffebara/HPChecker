package com.solomonm.HPChecker.config;

import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.List;

/**
 * HTTP 요청을 위한 Config
 * request timeout, baseURL 설정
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("api.mail")
public class EmailConfig {

    private String requestUrl;
    private String sender;
    private List<String> recipients;

    @Bean
    public WebClient webClient() {
        // 연결 timeout 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000); //10초

        // webClient request URL로 설정
        return WebClient.builder()
                .baseUrl(requestUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
