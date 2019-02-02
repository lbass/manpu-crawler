package com.manpu.crawler.worker.crawler;

import com.manpu.crawler.dto.WebtoonDto;

public interface WebtoonCrawler {

    WebtoonDto process(WebtoonDto workInfo);
}
