package com.gameplanner.game.dto;

import com.gameplanner.game.domain.Game;
import com.gameplanner.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameData {
    private Long id;
    private String name;
    private String cover;
    private String first_release_date;
    private List<String> genres;
    private List<String> platforms;
    private List<String> screenshots;
    private List<String> videos;

    public GameData(Game game){
        this.id = game.getId();
        this.name = game.getName();
        this.cover = game.getCover();
        this.genres = game.getGenres();
        long unixTime = game.getFirstReleaseDate();
        this.first_release_date = DateUtils.formatUnixTime(unixTime,"yyyy.MM.dd");
        this.platforms = game.getGamePlatforms().stream()
                .map(gamePlatform -> gamePlatform.getPlatform().getName())
                .collect(Collectors.toList());
        this.screenshots = game.getScreenshots();
        this.videos = game.getVideos();
    }
}
