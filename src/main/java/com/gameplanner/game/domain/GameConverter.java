package com.gameplanner.game.domain;

import com.gameplanner.client.igdb.IGDBGameResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Component
public class GameConverter {
    public Game convert(IGDBGameResponse response) {
        Game game = Game.builder()
                .id(response.getId())
                .name(response.getName())
                .firstReleaseDate(response.getFirst_release_date())
                .genres(Optional.ofNullable(response.getGenres())
                        .map(genres -> genres.stream().map(GameGenre::getNameById).collect(Collectors.toList())).orElseGet(Collections::emptyList))
                .screenshots(new ArrayList<>())
                .videos(new ArrayList<>())
                .gamePlatforms(new ArrayList<>())
                .build();

        return game;
    }
}
