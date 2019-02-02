package com.manpu.crawler.pagecrawler.lezhin.v2;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.lezhin.v1.LezhinWebtoonCrawler;
import com.manpu.crawler.worker.crawler.lezhin.v2.LezhinListCrawlerV2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LezhinListCrawlerV2.class, LezhinWebtoonCrawler.class, CrawlerMetric.class})
public class LezhinTest {

	private Logger logger = LoggerFactory.getLogger(LezhinTest.class);

	@Autowired
	private LezhinListCrawlerV2 lezhinListCrawler;

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
		webtoonDto.setUrl(LEZHIN_CONSTANT.COMIC_HOST + "/ko/comic/jisoo");
		WebtoonDto result = lezhinWebtoonCrawler.process(webtoonDto);
		logger.info("data {}", result);
		logger.info("title {}", result.getWebtoonTitle());
		logger.info("last episode num: {}", result.getEpisodeNumber());
		logger.info("pay episode num: {}", result.getPayEpisodeNumbers());
	}

}
