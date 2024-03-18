package com.gameplanner.batch;

import com.gameplanner.client.igdb.IGDBClient;
import com.gameplanner.client.igdb.IGDBGameResponse;
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
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestJob {
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final IGDBClient igdbClient;
    private final GameConverter gameConverter;


    @Bean
    public Job testSimpleJob(JobRepository jobRepository, Step testStep) {
        return new JobBuilder("testSimpleJob", jobRepository)
                .start(testStep)
                .build();
    }


    @JobScope
    @Bean
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("testStep", jobRepository)
                .<IGDBGameResponse, Game>chunk(100, platformTransactionManager)
                .reader(igdbItemReader())
                .processor(igdbItemProcessor())
                .writer(gameWriter())
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Game> gameWriter() {
        return new RepositoryItemWriterBuilder<Game>()
                .repository(gameRepository)
                .methodName("save")
                .build();
    }


    @StepScope
    @Bean
    public ItemProcessor<IGDBGameResponse, Game> igdbItemProcessor() {
        return new IGDBItemProcessor(platformRepository, gameConverter);
    }

    @StepScope
    @Bean
    public IGDBItemReader igdbItemReader() {
        return new IGDBItemReader(igdbClient);
    }

}
