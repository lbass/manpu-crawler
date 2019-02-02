package com.manpu.crawler.worker;

import com.manpu.crawler.cache.CrawlingWorkQueue;
import com.manpu.crawler.cache.ManpuCacheMap;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.worker.crawler.WebtoonCrawler;
import com.manpu.crawler.worker.crawler.daum.DaumWebtoonCrawler;
import com.manpu.crawler.worker.crawler.kakao.KakaoWebtoonCrawler;
import com.manpu.crawler.worker.crawler.lezhin.v1.LezhinWebtoonCrawler;
import com.manpu.crawler.worker.crawler.naver.NaverWebtoonCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class WebtoonWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(WebtoonWorker.class);

    @Autowired
    private CrawlingWorkQueue workQueue;

    @Autowired
    private ManpuCacheMap manpuCacheMap;

    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public void run() {
        try {
            WebtoonDto workInfo = workQueue.pollWorkInfo();
            if(workInfo == null) {
                return;
            }

            WebtoonCrawler webtoonCrawler = getCrawler(workInfo.getSite());

            logger.trace("process work {}", workInfo);
            WebtoonDto webtoonDto = webtoonCrawler.process(workInfo);
            if(webtoonDto == null) {
                return;
            }
            if(!SerialState.ACTIVE.equals(webtoonDto.getSerialState())) {
                logger.debug("Serial state: {} - {}"
                        , webtoonDto.getWebtoonTitle()
                        , webtoonDto.getSerialState());
                return;
            }

            String site = webtoonDto.getSite();
            String webtoonId = webtoonDto.getCrawledId();

            boolean isUpdateCache = false;
            if(manpuCacheMap.isExist(site, webtoonId)) {
                if(isUpdate(webtoonDto)) {
                    isUpdateCache = true;
                }
            } else {
                isUpdateCache = true;
            }

            if(isUpdateCache && SerialState.ACTIVE.equals(webtoonDto.getSerialState())) {
                logger.debug("update webtoon data: target - {} {}",
                        webtoonDto.getSite(), webtoonDto.getWebtoonTitle());
                workQueue.addUpdateInfo(webtoonDto);
                manpuCacheMap.updateWebtoonCache(webtoonDto.getSite(),
                        webtoonDto.getCrawledId(), webtoonDto);
            }
        } catch (Exception e) {
            logger.error("worker error {}", e);
        }
    }

    private boolean isUpdate(WebtoonDto webtoonDto) {
        long lastEpiNumber = manpuCacheMap.getLastEpiNumber(webtoonDto.getSite(), webtoonDto.getCrawledId());
        Map<WEEK, Integer> lastRanking =
                manpuCacheMap.getLastRankingMap(webtoonDto.getSite(), webtoonDto.getCrawledId());

        if(lastEpiNumber < webtoonDto.getEpisodeNumber()) {
            logger.debug("Last episode check [{} {}] {} -> {}"
                    , webtoonDto.getSite()
                    , webtoonDto.getWebtoonTitle()
                    , lastEpiNumber
                    , webtoonDto.getEpisodeNumber());
            return true;
        }

        if(lastRanking == null) {
            logger.debug("last ranking is null [{} {}]"
                    , webtoonDto.getSite()
                    , webtoonDto.getWebtoonTitle());
            return true;
        }

        Map<WEEK, Integer> weekInfo = webtoonDto.getWeekInfo();
        for( Entry<WEEK, Integer> e : weekInfo.entrySet() ){
            if(lastRanking.get(e.getKey()) == null ||
                    lastRanking.get(e.getKey()).intValue() != e.getValue().intValue()) {
                logger.debug("Ranking check [{} {}] {} {} -> {}"
                        , webtoonDto.getSite()
                        , webtoonDto.getWebtoonTitle()
                        , e.getKey()
                        , lastRanking.get(e.getKey())
                        , e.getValue());
                return true;
            }
        }

        return false;
    }

    private WebtoonCrawler getCrawler(String site) {
        try {
            SITE.valueOf(site);
        } catch (Exception e) {
            return null;
        }

        WebtoonCrawler crawler = null;
        switch(SITE.valueOf(site)) {
            case NAVER: {
                crawler = applicationContext.getBean(NaverWebtoonCrawler.class);
                break;
            }
            case DAUM: {
                crawler = applicationContext.getBean(DaumWebtoonCrawler.class);
                break;
            }
            case LEZHIN: {
                crawler = applicationContext.getBean(LezhinWebtoonCrawler.class);
                break;
            }
            case KAKAO: {
                crawler = applicationContext.getBean(KakaoWebtoonCrawler.class);
                break;
            }
            default: {
                break;
            }
        }
        return crawler;
    }
}
