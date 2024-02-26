package com.gameplanner.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // 사용자 이름으로 사용자 상세 정보를 로드하는 메서드
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {

        // 사용자 이름으로 사용자 정보를 찾아 UserDetails로 변환하거나, 찾지 못하면 예외를 발생시킴
        return userRepository.findByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }


    // User 엔티티 정보를 UserDetails로 변환하는 메서드
    private org.springframework.security.core.userdetails.User createUser(String username, User user) {

        // 사용자가 활성화되어 있지 않으면 예외를 발생시킴
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        // 사용자의 권한 목록을 가져와서 SimpleGrantedAuthority 객체로 변환한 후 리스트로 만듬
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getAuthorityType().name()));

        // UserDetails 객체를 생성하여 반환
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}
