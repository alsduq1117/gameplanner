package com.gameplanner.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


// 일반적인 Servlet Filter를 위한 기본 클래스인 GenericFilterBean을 상속받음
public class JwtFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";  // 인증 헤더 이름
    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // 실제 필터링 로직을 수행하는 메서드
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;   // ServletRequest를 HttpServletRequest로 캐스팅
        String jwt = resolveToken(httpServletRequest);  // 요청으로부터 JWT 토큰을 가져옴
        String requestURI = httpServletRequest.getRequestURI();   // 요청 URI를 가져옴

        // JWT 토큰이 존재하고 유효하다면, 토큰으로부터 인증 정보를 가져와 Security Context에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);  // 토큰으로부터 인증 정보를 가져옴
            SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증 정보를 Security Context에 저장
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        // 다음 필터로 요청과 응답을 전달
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 요청 헤더로부터 "Bearer "를 제외한 JWT 토큰을 가져오는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);  // Authorization 헤더로부터 Bearer 토큰을 가져옴

        // Bearer 토큰이 존재하면 "Bearer "를 제외한 나머지를 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;  // Bearer 토큰이 없으면 null을 반환
    }
}