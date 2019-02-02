package com.manpu.crawler.pagecrawler.naver.batch;

import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllWebToonDataCollector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@SpringBootTest(classes = {TestConfiguration.class})
public class NaverWebToonBatchCollectTest {
    private Logger logger = LoggerFactory.getLogger(NaverEpisodeBatchCollectTest.class);

    @Autowired
    private NaverAllWebToonDataCollector naverAllWebToonDataCollector;

    @Autowired
    private WebtoonService webtoonService;

    @Test
    public void naverMainPageCrawlerTest() {
    }
}
