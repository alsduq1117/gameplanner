package com.gameplanner.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Platform {

    @Id
    @Column(name = "platform_id")
    private Long Id;

    private String name;

    @OneToMany(mappedBy = "platform")
    private List<GamePlatform> gamePlatforms = new ArrayList<>();

    @Builder
    public Platform(Long id, String name) {
        this.Id = id;
        this.name = name;
    }
}
