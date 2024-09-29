package com.gameplanner.game.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "game_platform")
public class GamePlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_platform_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    @JsonIgnore
    private Platform platform;

    public GamePlatform(Game game, Platform platform) {
        this.game = game;
        this.platform = platform;
    }
}
