package com.manpu.crawler.pagecrawler.daum;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.daum.DaumListCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DaumListCrawler.class, CrawlerMetric.class})
public class DaumManagerCrawlerTest {

	@Autowired
	private DaumListCrawler daumListCrawler;

	@Test
	public void test() {
		daumListCrawler.work(WEEK.tue);
		daumListCrawler.work(WEEK.wed);
		daumListCrawler.work(WEEK.fri);
		daumListCrawler.work(WEEK.sat);
		daumListCrawler.work(WEEK.sun);
		daumListCrawler.work(WEEK.mon);
		daumListCrawler.work(WEEK.thu);
	}
}
