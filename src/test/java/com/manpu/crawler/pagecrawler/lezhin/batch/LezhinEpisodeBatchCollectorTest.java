package com.manpu.crawler.pagecrawler.lezhin.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.dto.EpisodeDto;
import com.manpu.crawler.entity.Episode;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.lezhin.v1.batch.LezhinAllEpisodeDataCollector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(classes = {
        LezhinAllEpisodeDataCollector.class,
        WebtoonService.class
})
@EnableTransactionManagement
public class LezhinEpisodeBatchCollectorTest {

    private Logger logger = LoggerFactory.getLogger(LezhinEpisodeBatchCollectorTest.class);

    @Autowired
    private LezhinAllEpisodeDataCollector lezhinAllEpisodeDataCollector;

    @Autowired
    private WebtoonService webtoonService;

    @MockBean
    private CrawlerMetric crawlerMetric;

    @Rollback(false)
    @Test
    public void naverMainPageCrawlerTest() {
        List<Webtoon> webtoonList = webtoonService.selectWebtoonListBySite(SITE.LEZHIN);
        for (int i = 0; i < webtoonList.size(); i++) {
            insertEpisodeData(webtoonList.get(i));
        }
    }

    private void insertEpisodeData(Webtoon webtoon) {
        String crawledId = webtoon.getCrawledId();
        try {
            List<EpisodeDto> episodeDtos = lezhinAllEpisodeDataCollector.work(crawledId);
            for (EpisodeDto episodeDto : episodeDtos) {
                if (episodeDto != null) {
                    Episode episode = new Episode();
                    episode.setWebtoon(webtoon);
                    episode.setFree(episodeDto.getFree());
                    episode.setTitle(episodeDto.getEpisodeTitle());
                    episode.setThumbnailUrl(episodeDto.getEpisodeThumbnailUrl());
                    episode.setNum(episodeDto.getEpisodeNumber());
                    episode.setRating(episodeDto.getRating());
                    episode.setUploadAt(DateHelper.getDateStringToDateTime(
                            episodeDto.getUpdateDate(), "yyyyMMdd"));
                    episode.setUrl(episodeDto.getUrl());

                    // logger.info("{}", episode);

                    Episode selectedEpisode = webtoonService.selectEpisode(
                            episode, episodeDto.getEpisodeNumber());
                    if (selectedEpisode != null) {
                        // logger.info("exist episode: {}", selectedEpisode);
                    } else {
                        webtoonService.insertEpisode(episode);
                        // logger.info("episode: {}", episode);
                    }

                }
            }
        } catch (Exception e) {
            logger.info("webtoon error: {}", crawledId);
        }
    }

}
