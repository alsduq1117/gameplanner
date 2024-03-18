package com.gameplanner.batch;

import com.gameplanner.client.igdb.IGDBGameResponse;
import com.gameplanner.game.domain.Game;
import com.gameplanner.game.domain.GameConverter;
import com.gameplanner.game.domain.GamePlatform;
import com.gameplanner.game.domain.Platform;
import com.gameplanner.game.repository.PlatformRepository;
import lombok.Data;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;


@Data
public class IGDBItemProcessor implements ItemProcessor<IGDBGameResponse, Game> {

    private final PlatformRepository platformRepository;
    private final GameConverter gameConverter;


    @Override
    public Game process(IGDBGameResponse item) throws Exception {
        Game game = gameConverter.convert(item);
        List<Long> platformIds = item.getPlatforms();
        if (platformIds != null) {
            List<Platform> allPlatformById = platformRepository.findAllById(platformIds);
            for (Platform platform : allPlatformById) {
                GamePlatform gamePlatform = new GamePlatform(game, platform);
                game.getGamePlatforms().add(gamePlatform);
                platform.getGamePlatforms().add(gamePlatform);
            }
        }
        return game;
    }

}
