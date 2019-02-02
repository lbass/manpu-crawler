package com.manpu.crawler.worker.crawler.daum;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_SERVICE_TYPE;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.WebtoonCrawler;
import com.manpu.crawler.worker.crawler.daum.model.DaumDetailResponse;
import com.manpu.crawler.worker.crawler.daum.model.DaumEpisode;
import com.manpu.crawler.worker.crawler.daum.model.DaumWebtoonData;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DaumWebtoonCrawler implements WebtoonCrawler {

    private Logger logger = LoggerFactory.getLogger(DaumWebtoonCrawler.class);

    @Autowired
    private CrawlerMetric crawlerMetric;

    @Override
    public WebtoonDto process(WebtoonDto webtoonDto) {
        final String url = webtoonDto.getUrl();

        logger.trace("collet url {}", url);
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (SocketTimeoutException e) {
            logger.error("Daum timeout error: {}", url);
            return null;
        } catch (IOException e) {
            logger.error("Daum io error error: {}", url);
            return null;
        }

        logger.trace("Daum res data {}", document.body().html());
        DaumDetailResponse daumResponse = JacksonJsonHelper.convertToObject(DaumDetailResponse.class, document.body().html());
        if(String.valueOf(HttpStatus.FORBIDDEN.value()).equalsIgnoreCase(daumResponse.getResult().getStatus())) {
            logger.warn("Daum adult webtoon: {}", webtoonDto.getWebtoonTitle());
            return null;
        }

        if(daumResponse.getData() != null) {
            DaumWebtoonData webtoonData = daumResponse.getData().getWebtoon();

            List<DaumEpisode> episodes = webtoonData.getWebtoonEpisodes();
            int lastEpisodeIndex = 0;
            int lastEpisodeNum = 1;
            List<Long> payEpisodes = new ArrayList<>();
            for(int i = 0 ; i < episodes.size() ; i++) {
                DaumEpisode episode = episodes.get(i);
                if(episode.getEpisodeNumber() > lastEpisodeNum) {
                    if(DAUM_SERVICE_TYPE.free.name().equalsIgnoreCase(episode.getServiceType())) {
                        lastEpisodeNum = episode.getEpisodeNumber();
                        lastEpisodeIndex = i;
                    }
                }
                /*
                if (!DAUM_SERVICE_TYPE.free.name().equalsIgnoreCase(episode.getServiceType())) {
                    payEpisodes.add((long)episode.getEpisodeNumber());
                }
                */
            }

            logger.debug("{} {} : last episode {}", webtoonDto.getCrawledId(), webtoonDto.getWebtoonTitle(), lastEpisodeNum);
            DaumEpisode lastEpisode = episodes.get(lastEpisodeIndex);
            webtoonDto.setEpisodeUpdateDate(lastEpisode.getDateCreated().substring(0, 8));
            webtoonDto.setEpisodeUrl(DAUM_CONSTANT.VIEWER_PATH + String.valueOf(lastEpisode.getEpisodeId()));
            webtoonDto.setEpisodeRating("");
            webtoonDto.setEpisodeNumber(lastEpisode.getEpisodeNumber());
            webtoonDto.setEpisodeThumbnailUrl(lastEpisode.getEpisodeThumbnailImage().getImageUrl());
            webtoonDto.setEpisodeTitle(lastEpisode.getTitle());
            webtoonDto.setLastUpdateDt(lastEpisode.getDateCreated().substring(0, 8));
            webtoonDto.setPayEpisodeNumbers(payEpisodes);
        } else {
            logger.error("Daum detail crawling is null");
            webtoonDto = null;
        }

        crawlerMetric.addDaumEpisodeWorkCount();

        return webtoonDto;
    }
}
