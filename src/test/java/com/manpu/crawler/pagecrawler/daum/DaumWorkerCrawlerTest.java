package com.manpu.crawler.pagecrawler.daum;


import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.daum.DaumWebtoonCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DaumWebtoonCrawler.class, CrawlerMetric.class})
public class DaumWorkerCrawlerTest {

    private Logger logger = LoggerFactory.getLogger(DaumWorkerCrawlerTest.class);

    @Autowired
    private DaumWebtoonCrawler daumWebtoonCrawler;

    @Test
    public void test() {
        WebtoonDto webtoonDto = new WebtoonDto();
        webtoonDto.setCrawledId("HeavenToHell");
        WebtoonDto result = daumWebtoonCrawler.process(webtoonDto);
        if(result != null) {
            logger.info("title {}", result.getWebtoonTitle());
            logger.info("last episode num: {}", result.getEpisodeNumber());
            logger.info("pay episode num: {}", result.getPayEpisodeNumbers());
        } else {
            logger.info("adult webtoon");
        }
        // logger.info("is freed {}", result.isFree());
    }
}

