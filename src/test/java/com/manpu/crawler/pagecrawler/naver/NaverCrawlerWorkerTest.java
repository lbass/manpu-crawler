package com.manpu.crawler.pagecrawler.naver;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.naver.NaverListCrawler;
import com.manpu.crawler.worker.crawler.naver.NaverWebtoonCrawler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NaverListCrawler.class, NaverWebtoonCrawler.class, CrawlerMetric.class})
public class NaverCrawlerWorkerTest {

    private Logger logger = LoggerFactory.getLogger(NaverCrawlerWorkerTest.class);

    @Autowired
    private NaverListCrawler naverListCrawler;

    @Autowired
    private NaverWebtoonCrawler naverWebtoonCrawler;


    // @Test
    public void naverListCrawlerTest() {
        WEEK[] weeks = WEEK.values();
        for(int i = 0 ; i < weeks.length ; i++) {
            List<WebtoonDto> webtoonList = naverListCrawler.work(weeks[i]);
            Assert.assertTrue(webtoonList != null && webtoonList.size() > 0);
        }
    }


    @Test
    public void naverWebToonCrawlerTest() {
        WEEK[] weeks = WEEK.values();
        List<WebtoonDto> webtoonDtos = new ArrayList<>();
        for(int i = 0 ; i < weeks.length ; i++) {
            List<WebtoonDto> webtoonList = naverListCrawler.work(weeks[i]);
            webtoonDtos.addAll(webtoonList);
        }

        webtoonDtos.stream().forEach(w -> naverWebtoonCrawler.process(w));
    }


    // @Test
    public void naverWebToonSingleCrawlerTest() {
        WebtoonDto webtoonDto = new WebtoonDto();
        webtoonDto.setUrl("/webtoon/list.nhn?titleId=616239&weekday=sat");
        naverWebtoonCrawler.process(webtoonDto);
    }

}
