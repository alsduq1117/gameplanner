package com.gameplanner.batch;

import com.gameplanner.client.igdb.*;
import com.gameplanner.game.domain.Game;
import com.gameplanner.game.domain.GameConverter;
import com.gameplanner.game.repository.GameRepository;
import com.gameplanner.game.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestJob {
    private final GameRepository gameRepository;
    private final GameConverter gameConverter;
    private final PlatformRepository platformRepository;
    private final IGDBClient igdbClient;


    @Bean
    public Job testSimpleJob(JobRepository jobRepository, Step testStep, Step insertScreenshotsStep, Step insertVideosStep, Step insertCoverStep) {
        return new JobBuilder("testSimpleJob", jobRepository)
                .start(testStep)
                .next(insertCoverStep)
                .next(insertScreenshotsStep)
                .next(insertVideosStep)
                .build();
    }


    @JobScope
    @Bean
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("testStep", jobRepository)
                .<IGDBGameResponse, Game>chunk(100, platformTransactionManager)
                .reader(igdbGameItemReader())
                .processor(igdbItemProcessor())
                .writer(gameWriter())
                .build();
    }

    @StepScope
    @Bean
    public IGDBGameItemReader igdbGameItemReader() {
        return new IGDBGameItemReader(igdbClient);
    }


    @StepScope
    @Bean
    public ItemProcessor<IGDBGameResponse, Game> igdbItemProcessor() {
        return new IGDBItemProcessor(platformRepository, gameConverter);
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Game> gameWriter() {
        return new RepositoryItemWriterBuilder<Game>()
                .repository(gameRepository)
                .methodName("save")
                .build();
    }

    @JobScope
    @Bean
    public Step insertScreenshotsStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("insertScreenshotsStep", jobRepository)
                .<Game, Game>chunk(100, platformTransactionManager)
                .reader(gameReader(gameRepository))
                .processor(screenshotProcessor())
                .writer(screenshotWriter())
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Game> gameReader(GameRepository gameRepository) {
        Map<String, Sort.Direction> sortKeys = new HashMap<>();
        sortKeys.put("id", Sort.Direction.ASC); // "id" 필드를 기준으로 오름차순 정렬

        return new RepositoryItemReaderBuilder<Game>()
                .name("gameReader")
                .repository(gameRepository)
                .methodName("findAll")
                .sorts(sortKeys)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Game, Game> screenshotProcessor() {
        return game -> {
            List<IGDBScreenshotResponse> screenshots = igdbClient.getScreenshots("fields image_id; where game = " + game.getId() + ";");
            List<String> screenshotIds = screenshots.stream().map(IGDBScreenshotResponse::getImage_id).collect(Collectors.toList());
            game.getScreenshots().addAll(screenshotIds);
            return game;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Game> screenshotWriter() {
        return games -> gameRepository.saveAll(games);
    }

    @JobScope
    @Bean
    public Step insertVideosStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("insertVideosStep", jobRepository)
                .<Game, Game>chunk(100, platformTransactionManager)
                .reader(gameReader(gameRepository)) //
                .processor(videoProcessor())
                .writer(videoWriter())
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Game, Game> videoProcessor() {
        return game -> {
            List<IGDBVideoResponse> videos = igdbClient.getVideos("fields video_id; where game = " + game.getId() + ";");
            List<String> videoIds = videos.stream().map(IGDBVideoResponse::getVideo_id).collect(Collectors.toList());
            game.getVideos().addAll(videoIds);
            return game;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Game> videoWriter() {
        return games -> gameRepository.saveAll(games);
    }


    @JobScope
    @Bean
    public Step insertCoverStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("insertCoverStep", jobRepository)
                .<Game, Game>chunk(100, platformTransactionManager)
                .reader(gameReader(gameRepository)) //
                .processor(coverProcessor())
                .writer(coverWriter())
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Game, Game> coverProcessor() {
        return game -> {
            List<IGDBCoverResponse> cover = igdbClient.getCover("fields image_id; where game = " + game.getId() + ";");
            if (!cover.isEmpty()) {
                game.updateCover(cover.get(0).getImage_id());
                return game;
            }
            return null;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Game> coverWriter() {
        return games -> gameRepository.saveAll(games);
    }

}
