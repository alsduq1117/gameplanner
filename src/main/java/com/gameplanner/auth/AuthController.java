package com.gameplanner.auth;

import com.gameplanner.jwt.JwtFilter;
import com.gameplanner.jwt.TokenDto;
import com.gameplanner.jwt.TokenProvider;
import com.gameplanner.user.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // "/api/authenticate" 경로로 POST 요청이 오면 실행되는 메서드
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 로그인 요청 정보로부터 사용자 이름과 비밀번호를 이용해 UsernamePasswordAuthenticationToken 객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 인증 토큰을 이용하여 인증을 수행하고, 결과로 나온 인증 정보를 가져옴
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); // AuthenticationManager 는 UserDetailsService를 사용하여 사용자의 상세 정보를 불러옵니다.

        // 인증 정보를 Security Context에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 이용하여 JWT 토큰을 생성
        String jwt = tokenProvider.createToken(authentication);

        // 응답 헤더를 생성하고, JWT 토큰을 "Bearer "와 함께 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // 생성한 JWT 토큰을 body에 담아 ResponseEntity를 생성하여 반환
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
