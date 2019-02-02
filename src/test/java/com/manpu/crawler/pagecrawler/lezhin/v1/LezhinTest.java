package com.manpu.crawler.pagecrawler.lezhin.v1;

import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.lezhin.v1.LezhinListCrawler;
import com.manpu.crawler.worker.crawler.lezhin.v1.LezhinWebtoonCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LezhinListCrawler.class, LezhinWebtoonCrawler.class, CrawlerMetric.class})
public class LezhinTest {

	private Logger logger = LoggerFactory.getLogger(LezhinTest.class);

	@Autowired
	private LezhinListCrawler lezhinListCrawler;

	@Autowired
	private LezhinWebtoonCrawler lezhinWebtoonCrawler;

	@Test
	public void test() {
		List<WebtoonDto> webtoonDtoList = lezhinListCrawler.work();
		webtoonDtoList.stream().forEach( w -> logger.info("webtoon : {}", w) );
	}

	@Test
	public void testWebtoonCrawler() {
		WebtoonDto webtoonDto = new WebtoonDto();
		webtoonDto.setUrl("/ko/comic/jisoo");
		WebtoonDto result = lezhinWebtoonCrawler.process(webtoonDto);
		logger.info("data {}", result);
		logger.info("title {}", result.getWebtoonTitle());
		logger.info("last episode num: {}", result.getEpisodeNumber());
		logger.info("pay episode num: {}", result.getPayEpisodeNumbers());
	}
}
