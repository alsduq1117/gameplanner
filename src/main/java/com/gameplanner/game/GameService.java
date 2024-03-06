package com.gameplanner.game;

import com.gameplanner.client.igdb.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final IGDBClient igdbClient;
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;


    @Transactional
    public void insertGames() {
        List<IGDBGameResponse> gameList = getGameListFromIGDB();
        for (IGDBGameResponse response : gameList) {
            Game game = buildGameFromResponse(response);
            addPlatformsToGame(response, game);
            gameRepository.save(game);
        }

    }

    @Transactional
    public void insertScreenshots() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            List<IGDBScreenshotResponse> screenshots = igdbClient.getScreenshots("fields image_id; where game = " + game.getId() + ";");
            List<String> screenshotIds = screenshots.stream().map(IGDBScreenshotResponse::getImage_id).collect(Collectors.toList());
            game.getScreenshots().addAll(screenshotIds);
        }
    }

    @Transactional
    public void insertVideos() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            List<IGDBVideoResponse> videos = igdbClient.getVideos("fields video_id; where game = " + game.getId() + ";");
            List<String> videoIds = videos.stream().map(IGDBVideoResponse::getVideo_id).collect(Collectors.toList());
            game.getVideos().addAll(videoIds);
        }
    }

    @Transactional
    public void insertCover() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            List<IGDBCoverResponse> cover = igdbClient.getCover("fields image_id; where game = " + game.getId() + ";");
            if(!cover.isEmpty()) {
                game.updateCover(cover.get(0).getImage_id());
            }
        }
    }

    @Transactional
    public GameResponse getGameList(int page, int size, int year, int month) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstReleaseDate"));
        Page<Game> games = gameRepository.findAll(pageable);

        return new GameResponse(games,page, year, month);
    }

    @Transactional
    public void insertPlatforms() {
        List<IGDBPlatformResponse> PlatForms = igdbClient.getPlatForms("fields name; limit 300; sort id asc;");
        for (IGDBPlatformResponse response : PlatForms) {
            platformRepository.save(Platform.builder().id((long) response.getId()).name(response.getName()).build());
        }
    }

    private List<IGDBGameResponse> getGameListFromIGDB() {
        return igdbClient.getGameList("fields name,first_release_date,genres,platforms; limit 300; offset 1500; sort first_release_date desc;");
    }

    private Game buildGameFromResponse(IGDBGameResponse response) {
        return Game.builder()
                .id(response.getId())
                .name(response.getName())
                .firstReleaseDate(response.getFirst_release_date())
                .genres(getGenreNames(response))
                .videos(new ArrayList<>())
                .screenshots(new ArrayList<>())
                .gamePlatforms(new ArrayList<>())
                .build();
    }

    private List<String> getGenreNames(IGDBGameResponse response) {
        List<Integer> genreIds = response.getGenres();
        if (genreIds != null) {
            return genreIds.stream()
                    .map(GameGenre::getNameById)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private void addPlatformsToGame(IGDBGameResponse response, Game game) {
        List<Long> platformIds = response.getPlatforms();
        if (platformIds != null) {
            List<Platform> allPlatformById = platformRepository.findAllById(platformIds);
            for (Platform platform : allPlatformById) {
                GamePlatform gamePlatform = new GamePlatform(game, platform);
                game.getGamePlatforms().add(gamePlatform);
                platform.getGamePlatforms().add(gamePlatform);
            }
        }
    }


}
