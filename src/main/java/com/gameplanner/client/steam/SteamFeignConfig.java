package com.gameplanner.client.steam;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class SteamFeignConfig {

    @Bean
    Logger.Level SteamFeignLoggerLevel() {
        return Logger.Level.FULL; // log레벨 설정
    }

}
