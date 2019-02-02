package com.manpu.crawler.worker;

import com.manpu.crawler.cache.CrawlingWorkQueue;
import com.manpu.crawler.cache.ManpuCacheMap;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.service.PushService;
import com.manpu.crawler.service.WebtoonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostProcessWorker {
	private Logger logger = LoggerFactory.getLogger(PostProcessWorker.class);

	@Autowired
	private CrawlingWorkQueue crawlingWorkQueue;

	@Autowired
	private WebtoonService webtoonService;

	@Autowired
	private PushService pushService;

    @Autowired
    private ManpuCacheMap manpuCacheMap;
    
	public void process() {
		// snapshot count - 1회 작업량
		List<WebtoonDto> webtoonDtoList = new ArrayList<>();
		try {
			int workCount = crawlingWorkQueue.getUpdateInfoQueueSize();
			for(int i = 0 ; i < workCount ; i++) {
				webtoonDtoList.add(crawlingWorkQueue.pollUpdateInfo());
			}
			logger.info("current work count: {}", webtoonDtoList.size());
			List<WebtoonDto> episodeUpdateWebtoons = webtoonService.updateWebtoonData(webtoonDtoList);
			logger.trace("{}", episodeUpdateWebtoons);
			pushService.pushByUpdateWebtoons(episodeUpdateWebtoons);
		} catch (Exception e) {
			logger.error("PostProcessWorker error: {}", e);
			manpuCacheMap.removeWebtoonCache(webtoonDtoList);
		}
	}
}
