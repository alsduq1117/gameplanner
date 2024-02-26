package com.gameplanner.config;


import com.gameplanner.jwt.JwtAccessDeniedHandler;
import com.gameplanner.jwt.JwtAuthenticationEntryPoint;
import com.gameplanner.jwt.JwtSecurityConfig;
import com.gameplanner.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity  //web 보안 활성화
@EnableMethodSecurity  // 메서드 단위로 보안 적용
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter; // CORS 필터
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 유효한 자격증명을 제공하지 않고 접근하려 할 때 401 Error를 반환
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 필요한 권한이 존재하지 않는 경우에 403 Error를 반환


    // BCrypt 암호화 방식을 사용하는 PasswordEncoder Bean을 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // HttpSecurity를 통해 HTTP 요청에 대한 보안 설정을 한 후, SecurityFilterChain 객체를 반환
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross-Site Request Forgery) 방어 기능을 비활성화 (토큰 방식을 사용하기 때문)
                .csrf(AbstractHttpConfigurer::disable)

                // 요청에 대해 UsernamePasswordAuthenticationFilter 처리 전에 CORS 필터를 적용
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증이나 권한에 대한 예외를 처리하는 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)  // 필요한 권한이 없는 경우 처리할 핸들러
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 인증되지 않은 사용자가 접근했을 때 처리할 핸들러
                )

                // Http 요청에 대한 접근 제어 설정
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll()  // 해당 경로는 모두 허용
                        .requestMatchers( "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()  // Swagger 관련 경로는 모두 허용
                        .anyRequest().authenticated()  // 그 외 요청은 모두 인증 필요
                )

                // 세션 관리 설정 (세션을 사용하지 않으므로 STATELESS로 설정)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT 토큰 제공자를 사용하는 JwtSecurityConfig 적용
                .with(new JwtSecurityConfig(tokenProvider), customizer -> {});

        return http.build();
    }
}
