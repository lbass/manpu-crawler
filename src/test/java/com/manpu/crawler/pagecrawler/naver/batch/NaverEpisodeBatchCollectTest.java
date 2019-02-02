package com.manpu.crawler.pagecrawler.naver.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.dto.EpisodeDto;
import com.manpu.crawler.entity.Episode;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllEpisodeDataCollector;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllEpisodeDataCollector.InvalidPageException;
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
        NaverAllEpisodeDataCollector.class,
        WebtoonService.class
})
@EnableTransactionManagement
public class NaverEpisodeBatchCollectTest {

    private Logger logger = LoggerFactory.getLogger(NaverEpisodeBatchCollectTest.class);

    @Autowired
    private NaverAllEpisodeDataCollector naverAllEpisodeDataCollector;

    @Autowired
    private WebtoonService webtoonService;

    @MockBean
    private CrawlerMetric crawlerMetric;

    private static int MAX_PAGE_COUNT = 200;

    @Rollback(false)
    @Test
    public void naverMainPageCrawlerTest() {
        List<Webtoon> webtoonList = webtoonService.selectWebtoonListBySite(SITE.NAVER);
        for (int i = 0; i < webtoonList.size(); i++) {
            insertEpisodeData(webtoonList.get(i));
        }
    }

    private void insertEpisodeData(Webtoon webtoon) {
        String crawledId = webtoon.getCrawledId();
        for (int i = 1; i < MAX_PAGE_COUNT; i++) {
            try {
                List<EpisodeDto> episodeDtos = naverAllEpisodeDataCollector.work(crawledId, String.valueOf(i));
                for (EpisodeDto episodeDto : episodeDtos) {
                    if (episodeDto != null) {
                        Episode episode = new Episode();
                        episode.setWebtoon(webtoon);
                        episode.setFree(true);
                        episode.setTitle(episodeDto.getEpisodeTitle());
                        episode.setThumbnailUrl(episodeDto.getEpisodeThumbnailUrl());
                        episode.setNum(episodeDto.getEpisodeNumber());
                        episode.setRating(episodeDto.getRating());
                        episode.setUploadAt(DateHelper.getDateStringToDateTime(
                                episodeDto.getUpdateDate(), "yyyy.MM.dd"));
                        episode.setUrl(episodeDto.getUrl());
                        // logger.trace("{}", episodeDto);

                        Episode selectedEpisode = webtoonService.selectEpisode(
                                episode, episodeDto.getEpisodeNumber());
                        ;
                        // logger.info("episode: {}", episode);
                        if (selectedEpisode != null) {
                            logger.info("exist episode: {}", selectedEpisode);
                        } else {
                            webtoonService.insertEpisode(episode);
                            logger.info("episode: {}", episode);
                        }
                    }
                }
            } catch (InvalidPageException e) {
                logger.info("end page index");
                break;
            }
        }
    }

}
