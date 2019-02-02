package com.manpu.crawler.pagecrawler.configuration;

import com.manpu.crawler.repository.*;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllEpisodeDataCollector;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllWebToonDataCollector;
import com.manpu.crawler.worker.crawler.naver.batch.NaverGenreCollectWorker;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaAuditing
@EnableAutoConfiguration
@EntityScan("com.manpu.crawler.entity")
@EnableTransactionManagement
public class TestConfiguration {

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public NaverGenreCollectWorker naverGenreCollectWorker() {
        return new NaverGenreCollectWorker();
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public NaverAllEpisodeDataCollector naverAllEpisodeDataCollector() {
        return new NaverAllEpisodeDataCollector();
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public NaverAllWebToonDataCollector naverAllWebToonDataCollector() {
        return new NaverAllWebToonDataCollector();
    }

    @Bean(name="dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public WebtoonRepository WebtoonRepository() {
        return new WebtoonRepository();
    }

    @Bean
    public ArtistRepository artistRepository() {
        return new ArtistRepository();
    }

    @Bean
    public EpisodeRepository episodeRepository() {
        return new EpisodeRepository();
    }

    @Bean
    public WebtoonArtistRepository webtoonArtistRepository() {
        return new WebtoonArtistRepository();
    }

    @Bean
    public WebtoonWeekRepository webtoonWeekRepository() {
        return new WebtoonWeekRepository();
    }

}
