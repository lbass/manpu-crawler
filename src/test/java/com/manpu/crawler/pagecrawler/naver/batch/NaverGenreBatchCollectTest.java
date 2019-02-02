package com.manpu.crawler.pagecrawler.naver.batch;

import com.manpu.crawler.dto.GenreDto;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.naver.batch.NaverAllWebToonDataCollector;
import com.manpu.crawler.worker.crawler.naver.batch.NaverGenreCollectWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@SpringBootTest(classes = {TestConfiguration.class})
public class NaverGenreBatchCollectTest {

    private Logger logger = LoggerFactory.getLogger(NaverGenreBatchCollectTest.class);

    @Autowired
    private NaverGenreCollectWorker naverGenreCollectWorker;

    @Autowired
    private NaverAllWebToonDataCollector naverAllWebToonDataCollector;

    @Autowired
    private WebtoonService webtoonService;

    @Commit
    @Test
    public void naverMainPageCrawlerTest() {
        List<GenreDto> genreDtoList = naverGenreCollectWorker.work();

    }
}
