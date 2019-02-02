package com.manpu.crawler.cache;

import com.manpu.crawler.dto.WebtoonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class CrawlingWorkQueue {

	private Logger logger = LoggerFactory.getLogger(CrawlingWorkQueue.class);

	private ConcurrentLinkedQueue<WebtoonDto> workQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<WebtoonDto> updateWebtoonQueue = new ConcurrentLinkedQueue<>();

	public void addWorkInfo(WebtoonDto workInfo) {
		workQueue.add(workInfo);
	}

	public void addAllWorkInfo(List<WebtoonDto> webtoonDtoList) {
		workQueue.addAll(webtoonDtoList);
	}

	public WebtoonDto pollWorkInfo() {
		return workQueue.poll();
	}

	public int getWorkInfoQueryrSize() {
		return workQueue.size();
	}

	public void addUpdateInfo(WebtoonDto workInfo) {
		updateWebtoonQueue.add(workInfo);
	}

	public void addAllUpdateInfo(List<WebtoonDto> webtoonDtoList) {
		updateWebtoonQueue.addAll(webtoonDtoList);
	}

	public WebtoonDto pollUpdateInfo() {
		return updateWebtoonQueue.poll();
	}

	public int getUpdateInfoQueueSize() {
		return updateWebtoonQueue.size();
	}
}
