package com.gameplanner.game.domain;

import com.gameplanner.client.igdb.IGDBGameResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "game_id")
    private Long id;

    private String name;

    private String cover;

    @Column(name = "first_release_date")
    private Long firstReleaseDate;

    @ElementCollection
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> genres = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "game_screenshots", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> screenshots = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_videos", joinColumns = @JoinColumn(name = "game_id"))
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

    @Builder
    public Game(IGDBGameResponse response) {
        this.id = response.getId();
        this.name = response.getName();
        this.firstReleaseDate = response.getFirst_release_date();
        this.genres = Optional.ofNullable(response.getGenres())
                .map(genres -> genres.stream().map(GameGenre::getNameById).collect(Collectors.toList())).orElseGet(Collections::emptyList);
        this.screenshots = new ArrayList<>();
        this.videos = new ArrayList<>();
        this.gamePlatforms = new ArrayList<>();
    }


    public void updateCover(String cover) {
        this.cover = cover;
    }
}