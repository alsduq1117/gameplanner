package com.gameplanner.user;


import com.gameplanner.auth.AuthorityType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type")
    private AuthorityType authorityType;

    @Builder
    public User(String username, String password, String nickname, boolean activated, AuthorityType authorityType) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.activated = activated;
        this.authorityType = authorityType;
    }
}
