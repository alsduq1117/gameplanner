package com.gameplanner.client.opencritic;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class OpenCriticFeignConfig {

    @Bean
    Logger.Level OpenCriticFeignLoggerLevel() {
        return Logger.Level.FULL; // log레벨 설정
    }


    @Bean
    public RequestInterceptor openCriticRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("X-RapidAPI-Key", "50f426e81fmshc5b9491922ad630p167fa6jsn1687ff08aa4d");
            requestTemplate.header("X-RapidAPI-Host", "opencritic-api.p.rapidapi.com");
        };
    }


}
