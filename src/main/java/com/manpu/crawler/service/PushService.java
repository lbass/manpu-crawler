package com.manpu.crawler.service;

import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.helper.RestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushService {
	private Logger logger = LoggerFactory.getLogger(PushService.class);

	@Autowired
	private RestHelper restHelper;

	public void pushByUpdateWebtoons(List<WebtoonDto> episodeUpdateWebtoons) {
		if(episodeUpdateWebtoons != null && episodeUpdateWebtoons.size() > 0) {
			episodeUpdateWebtoons.stream().forEach(webtoonDto ->
					restHelper.pushWebtoonInfomation(
							String.format("%s-%s", webtoonDto.getSite(), webtoonDto.getCrawledId()),
							webtoonDto.getSite(),
							webtoonDto.getWebtoonTitle(),
							webtoonDto.getEpisodeTitle()
					)
			);
		}
	}
}
