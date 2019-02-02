package com.manpu.crawler.repository;


import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.pagecrawler.configuration.TestConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Import(TestConfiguration.class)
@SpringBootTest(classes = {WebtoonRepository.class})
public class WebtoonRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(WebtoonRepositoryTest.class);

    @Autowired
    private WebtoonRepository webtoonRepository;

    private List<Webtoon> webtoonList;

    @Before
    public void createTestData() {
        webtoonList = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i++) {
            Webtoon webtoon = new Webtoon();
            webtoon.setSite(SITE.NAVER.name());
            webtoon.setCrawledId("test" + i);
            webtoon.setRating("0.0" + i);
            webtoon.setTitle("test-title");
            webtoon.setLastEpisodeNum(i);
            webtoon.setSerialState(SerialState.ACTIVE);
            webtoon.setId(SITE.NAVER + "-" + webtoon.getCrawledId());
            webtoonList.add(webtoon);
        }
    }

    @Test
    @Rollback
    public void updateBatchRankingtest() {
        for(int i = 0 ; i < this.webtoonList.size() ; i++) {
            webtoonRepository.save(webtoonList.get(i));
        }

        for(int i = 0 ; i < this.webtoonList.size() ; i++) {
            Webtoon webtoon = webtoonList.get(i);
            logger.warn("{}", webtoon);
            webtoon.setRating("9.00");
            webtoonList.set(i, webtoon);
        }

        webtoonRepository.updateBatchRakingInfo(webtoonList);

        for(int i = 0 ; i < this.webtoonList.size() ; i++) {
            Webtoon webtoon = webtoonList.get(i);
            Webtoon seletedData = webtoonRepository.findOne(webtoon.getId());
            logger.warn("{}", seletedData);
            Assert.assertTrue(webtoon.getRating().equals(seletedData.getRating()));
        }
    }

    @Test
    public void findSerialWebtoonBySiteTest() {
        List<Webtoon> webtoonList = webtoonRepository.findNaverAndDaumWebtoon();
        final Map<String, String> ratingMap = new HashMap<>();
        webtoonList.stream()
                .filter(webtoonDto -> SITE.DAUM.name().equalsIgnoreCase(webtoonDto.getSite()))
                .forEach(webtoonDto -> ratingMap.put(webtoonDto.getCrawledId(), webtoonDto.getRating()));
        logger.warn("webtoonList: {} {}", webtoonList.size(), webtoonList);
        logger.warn("ratingMap: {} {}", ratingMap.size(), ratingMap);
    }
}
