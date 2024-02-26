package com.gameplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //CORS 정책 설정을 위한 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        //클라이언트가 요청 시 사용한 자격 증명(쿠키, 인증 헤더)를 허용
        config.setAllowCredentials(true);

        //모든 요청을 허용
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // "/api/**" 경로에 대해 위에서 설정한 CORS 정책을 적용합니다.
        source.registerCorsConfiguration("/api/**", config);

        // 생성된 CORS 설정을 가진 CorsFilter 객체를 반환합니다. 이 필터는 요청이 들어올 때 CORS 정책을 적용합니다.
        return new CorsFilter(source);
    }
}
