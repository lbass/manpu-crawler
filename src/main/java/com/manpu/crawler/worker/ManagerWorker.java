package com.manpu.crawler.worker;

import com.manpu.crawler.cache.CrawlingWorkQueue;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.daum.DaumListCrawler;
import com.manpu.crawler.worker.crawler.kakao.KakaoListCrawler;
import com.manpu.crawler.worker.crawler.lezhin.v2.LezhinListCrawlerV2;
import com.manpu.crawler.worker.crawler.naver.NaverListCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagerWorker {

    private Logger logger = LoggerFactory.getLogger(ManagerWorker.class);

    @Autowired
    private CrawlingWorkQueue workQueue;

    @Autowired
    private NaverListCrawler naverListCrawler;

    @Autowired
    private DaumListCrawler daumListCrawler;

    @Autowired
    private LezhinListCrawlerV2 lezhinListCrawler;

    @Autowired
    private KakaoListCrawler kakaoListCrawler;

    @Autowired
    private CrawlerMetric crawlerMetric;

    public void process() {

        if (workQueue.getWorkInfoQueryrSize() > MANPU_CRAWLER_CONSTANT.MAX_WORK_QUEUE_SIZE) {
            logger.warn("workQueue is busy (queue size: {})", workQueue.getWorkInfoQueryrSize());
            return;
        }

        WEEK[] weeks = WEEK.values();
        try {
            crawlerMetric.addNaverListWorkCount();
            int workCount = 0;
            for (int i = 0; i < weeks.length; i++) {
                if(weeks[i].equals(WEEK.na)) {
                    continue;
                }
                List<WebtoonDto> webtoonDtos = naverListCrawler.work(weeks[i]);
                if (webtoonDtos != null) {
                    workCount += webtoonDtos.size();
                    workQueue.addAllWorkInfo(webtoonDtos);
                }
            }
            logger.debug("naver webtoon count: {}", workCount);
        } catch (Exception e) {
            crawlerMetric.addNaverListWorkErrorCount();
            logger.error("naver list crawler error: {}", e);
        }

        try {
            crawlerMetric.addDaumListWorkCount();
            int workCount = 0;
            for (int i = 0; i < weeks.length; i++) {
                if(weeks[i].equals(WEEK.na)) {
                    continue;
                }
                List<WebtoonDto> webtoonDtos = daumListCrawler.work(weeks[i]);
                if (webtoonDtos != null) {
                    workCount += webtoonDtos.size();
                    workQueue.addAllWorkInfo(webtoonDtos);
                }
            }
            logger.debug("daum webtoon count: {}", workCount);
        } catch (Exception e) {
            crawlerMetric.addDaumListWorkErrorCount();
            logger.error("daum list crawler error: {}", e);
        }

        try {
            crawlerMetric.addLezhinListWorkCount();
            List<WebtoonDto> webtoonDtos = lezhinListCrawler.work();
            if (webtoonDtos != null) {
                logger.debug("lezhin webtoon count: {}", webtoonDtos.size());
                workQueue.addAllWorkInfo(webtoonDtos);
            }
        } catch (Exception e) {
            crawlerMetric.addLezhinListWorkErrorCount();
            logger.error("lezhin list crawler error: {}", e);
        }

        /** Kakao는 수집을 중지한다.
        try {
            crawlerMetric.addKakaoListWorkCount();
            List<WebtoonDto> webtoonDtos = kakaoListCrawler.work();
            if (webtoonDtos != null) {
                logger.debug("kakao webtoon count: {}", webtoonDtos.size());
                workQueue.addAllWorkInfo(webtoonDtos);
            }
        } catch (Exception e) {
            crawlerMetric.addKakaoListWorkErrorCount();
            logger.error("kakao list crawler error: {}", e);
        }
        */
    }
}
