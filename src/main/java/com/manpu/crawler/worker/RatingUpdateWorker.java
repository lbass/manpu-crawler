package com.manpu.crawler.worker;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.service.WebtoonService;
import com.manpu.crawler.worker.crawler.daum.DaumListCrawler;
import com.manpu.crawler.worker.crawler.naver.NaverWebtoonCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RatingUpdateWorker {

    private Logger logger = LoggerFactory.getLogger(RatingUpdateWorker.class);

    @Autowired
    private WebtoonService webtoonService;

    @Autowired
    private NaverWebtoonCrawler naverWebtoonCrawler;

    @Autowired
    private DaumListCrawler daumListCrawler;

    public void updateRating() {
        List<WebtoonDto> webtoonDtoList = webtoonService.getNaverAndDaumWebtoon();
        updateNaverRating(webtoonDtoList);

        final Map<String, String> ratingMap = new HashMap<>();
        webtoonDtoList.stream()
                .filter(webtoonDto -> SITE.DAUM.name().equalsIgnoreCase(webtoonDto.getSite()))
                .forEach(webtoonDto -> ratingMap.put(webtoonDto.getCrawledId(), webtoonDto.getRating()));
        updateDaumRating(ratingMap);
    }

    private void updateNaverRating(List<WebtoonDto> webtoonDtoList) {
        List<WebtoonDto> targetList = new ArrayList<>();
        for(int i = 0 ; i < webtoonDtoList.size() ; i++) {
            WebtoonDto webtoonDto = webtoonDtoList.get(i);
            if(SITE.NAVER.equals(webtoonDto.getSite())) {
                String originRating = webtoonDto.getRating();
                webtoonDto = naverWebtoonCrawler.process(webtoonDto);
                if(originRating == null || !originRating.equalsIgnoreCase(webtoonDto.getRating())) {
                    logger.debug(
                            "rating update {}, {} -> {}"
                            , webtoonDto.getWebtoonTitle()
                            , originRating
                            , webtoonDto.getRating());
                    targetList.add(webtoonDto);
                }
            }
        }
        if(targetList.size() > 0) {
            webtoonService.updatBatcheWebtoonRaking(targetList);
        }
    }

    private void updateDaumRating(Map<String, String> ratingMap) {
        // 주 단위로 웹툰을 수집하여 반복작업
        List<WebtoonDto> targetList = new ArrayList<>();
        for(WEEK week : WEEK.values()){
            if(week.equals(WEEK.na)) {
                continue;
            }

            List<WebtoonDto> recentDtos = daumListCrawler.work(week);
            for(int i = 0 ; i < recentDtos.size() ; i++) {
                WebtoonDto recentDto = recentDtos.get(i);
                String originRating = ratingMap.get(recentDto.getCrawledId());
                if(originRating == null || !recentDto.getRating().equalsIgnoreCase(originRating)) {
                    logger.debug(
                            "rating update {}, {} -> {}"
                            , recentDto.getWebtoonTitle()
                            , originRating
                            , recentDto.getRating());
                    targetList.add(recentDto);
                }
            }
        }

        if(targetList.size() > 0) {
            webtoonService.updatBatcheWebtoonRaking(targetList);
        }
    }
}
