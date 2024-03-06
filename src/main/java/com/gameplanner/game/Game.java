package com.gameplanner.game;

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
public class Game {

    @Id
    private Long id;

    private String name;

    private String cover;

    private Long firstReleaseDate;

    @ElementCollection
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> genres = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "game_screenshots" , joinColumns = @JoinColumn(name = "game_id"))
    private List<String> screenshots = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_videos" , joinColumns = @JoinColumn(name = "game_id"))
    private List<String> videos = new ArrayList<>();


    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GamePlatform> gamePlatforms = new ArrayList<>();


    @Builder
    public Game(Long id, String name, String cover, Long firstReleaseDate, List<String> genres, List<String> screenshots, List<String> videos, List<GamePlatform> gamePlatforms) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.firstReleaseDate = firstReleaseDate;
        this.genres = genres;
        this.screenshots = screenshots;
        this.videos = videos;
        this.gamePlatforms = gamePlatforms;
    }

    public void updateCover(String cover){
        this.cover = cover;
    }
}