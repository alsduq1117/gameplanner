package com.gameplanner.client.igdb;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class IGDBFeignConfig {

    @Bean
    Logger.Level IGDBFeignLoggerLevel() {
        return Logger.Level.FULL; // log레벨 설정
    }

    @Bean
    public RequestInterceptor IGDBRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Client-ID", "vctxh0bdooxd0s9mvtceyjpomo5v3a");
            requestTemplate.header("Authorization", "Bearer 3mx1nyzm0fjhs82labvfvh97jq1mql");  //유효기간 60일 정도 (하드코딩 주의)
        };
    }

}
