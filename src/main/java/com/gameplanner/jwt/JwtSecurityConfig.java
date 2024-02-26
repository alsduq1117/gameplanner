package com.gameplanner.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    // HttpSecurity 객체에 JWT 필터를 추가하는 설정
    @Override
    public void configure(HttpSecurity http) {
        // UsernamePasswordAuthenticationFilter 처리 전에 JwtFilter를 적용
        http.addFilterBefore(
                new JwtFilter(tokenProvider),  // JWT 처리를 위한 커스텀 필터
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
