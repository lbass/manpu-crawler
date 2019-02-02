package com.manpu.crawler.pagecrawler.kakao;

import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.kakao.KakaoListCrawler;
import com.manpu.crawler.worker.crawler.kakao.KakaoWebtoonCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KakaoListCrawler.class, KakaoWebtoonCrawler.class, CrawlerMetric.class})
public class KakaoTest {

	@Autowired
	private KakaoListCrawler kakaoListCrawler;

	@Autowired
	private KakaoWebtoonCrawler kakaoWebtoonCrawler;


	@Test
	public void test() {
		kakaoListCrawler.work();
	}


	@Test
	public void testWebtoonCrawler() {
		WebtoonDto webtoonDto = new WebtoonDto();
		webtoonDto.setUrl("https://page.kakao.com/home?seriesId=51506533");
		webtoonDto.setCrawledId("51506533");
		kakaoWebtoonCrawler.process(webtoonDto);
	}

}
