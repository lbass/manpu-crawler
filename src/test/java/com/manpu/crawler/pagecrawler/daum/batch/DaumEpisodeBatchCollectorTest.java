package com.manpu.crawler.pagecrawler.daum.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.dto.EpisodeDto;
import com.manpu.crawler.entity.Episode;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import com.manpu.crawler.pagecrawler.naver.batch.NaverEpisodeBatchCollectTest;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.daum.batch.DaumAllEpisodeDataCollector;
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
        DaumAllEpisodeDataCollector.class,
        WebtoonService.class
})
@EnableTransactionManagement
public class DaumEpisodeBatchCollectorTest {

    private Logger logger = LoggerFactory.getLogger(NaverEpisodeBatchCollectTest.class);

    @Autowired
    private DaumAllEpisodeDataCollector daumAllEpisodeDataCollector;

    @Autowired
    private WebtoonService webtoonService;

    @MockBean
    private CrawlerMetric crawlerMetric;

    @Rollback(false)
    @Test
    public void daumMainPageCrawlerTest() {
        List<Webtoon> webtoonList = webtoonService.selectWebtoonListBySite(SITE.DAUM);
        for (int i = 0; i < webtoonList.size(); i++) {
            insertEpisodeData(webtoonList.get(i));
        }
    }

    private void insertEpisodeData(Webtoon webtoon) {
        String crawledId = webtoon.getCrawledId();
        List<EpisodeDto> episodeDtos = daumAllEpisodeDataCollector.work(crawledId);
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
                // logger.trace("{}", episodeDto);


                Episode selectedEpisode = webtoonService.selectEpisode(
                        episode, episodeDto.getEpisodeNumber());
                if (selectedEpisode != null) {
                    logger.info("exist episode: {}", selectedEpisode);
                } else {
                    webtoonService.insertEpisode(episode);
                    logger.info("episode: {}", episode);
                }
            }
        }
    }

}
