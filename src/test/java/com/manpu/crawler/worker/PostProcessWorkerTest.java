package com.manpu.crawler.worker;

import com.manpu.crawler.cache.CrawlingWorkQueue;
import com.manpu.crawler.cache.ManpuCacheMap;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.WebtoonWeek;
import com.manpu.crawler.service.WebtoonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostProcessWorker.class, ManpuCacheMap.class})
public class PostProcessWorkerTest {

	private Logger logger = LoggerFactory.getLogger(PostProcessWorkerTest.class);
	
	@MockBean(name="crawlingWorkQueue")
	private CrawlingWorkQueue crawlingWorkQueue;
	
	@MockBean
	private WebtoonService webtoonService;

	@Autowired
	private ManpuCacheMap manpuCacheMap;
	
	@Autowired
	PostProcessWorker postProcessWorker;

	@Before
	public void init() {

		Webtoon webtoon = new Webtoon();
		webtoon.setId("TEST-WEBTOON");
		webtoon.setCrawledId("WEBTOON");
		webtoon.setSite(SITE.NAVER.name());
		List<Webtoon> webtoonList = new ArrayList<>();
		webtoonList.add(webtoon);

		WebtoonWeek webtoonWeek = new WebtoonWeek();
		webtoonWeek.setWebtoon(webtoon);
		List<WebtoonWeek> webtoonWeekList = new ArrayList<>();
		webtoonWeekList.add(webtoonWeek);

		when(webtoonService.getWebtoonInSerial()).thenReturn(webtoonList);
		when(webtoonService.getWebtoonWeekInfo(webtoon)).thenReturn(webtoonWeekList);

		WebtoonDto webtoonDto = new WebtoonDto();
		webtoonDto.setSite(SITE.NAVER.name());
		webtoonDto.setCrawledId("WEBTOON");
		given(crawlingWorkQueue.getUpdateInfoQueueSize()).willReturn(1);
		given(crawlingWorkQueue.pollUpdateInfo()).willReturn(webtoonDto);

		Exception expectedException = new Exception("test exception");
		doThrow(new RuntimeException()).doNothing().when(webtoonService).updateWebtoonData(anyList());

	}
	
	@Test
	public void testProcess() {
		manpuCacheMap.collectWebtoonCache();

		int beforeCount = manpuCacheMap.getMapCount(SITE.NAVER.name());

		//TODO webtoonService.updateWebtoonData에서 예외 발생 시켜야 함.
		postProcessWorker.process();

		int afterCount = manpuCacheMap.getMapCount(SITE.NAVER.name());

		logger.info("before: {} | after : {}", beforeCount, afterCount);
		Assert.assertTrue(afterCount == 0);
	}

}
