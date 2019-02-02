package com.manpu.crawler.worker.crawler.kakao;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.KAKAO_CONSTANT;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.WebtoonCrawler;
import com.manpu.crawler.worker.crawler.kakao.model.KakaoDetailDataRoot;
import com.manpu.crawler.worker.crawler.kakao.model.KakaoWebtoonSingle;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

@Component
public class KakaoWebtoonCrawler implements WebtoonCrawler {

    private Logger logger = LoggerFactory.getLogger(KakaoWebtoonCrawler.class);

    @Autowired
    private CrawlerMetric crawlerMetric;

    @Override
    public WebtoonDto process(WebtoonDto webtoonDto) {
        final String url = KAKAO_CONSTANT.DETAIL_API_PATH;
        Document document = null;
        try {
            document = JsoupHelper.connectPostWithParam(url,
                    new SimpleEntry<>("seriesid", webtoonDto.getCrawledId()),
                    new SimpleEntry<>("page", "0"),
                    new SimpleEntry<>("direction", "desc"),
                    new SimpleEntry<>("page_size", "1"),
                    new SimpleEntry<>("without_hidden", "true")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        crawlerMetric.addKakaoEpisodeWorkCount();
        KakaoDetailDataRoot kakaoDetailDataRoot =
                JacksonJsonHelper.convertToObject(KakaoDetailDataRoot.class, document.body().html());
        logger.trace(kakaoDetailDataRoot.toString());


        KakaoWebtoonSingle episode = kakaoDetailDataRoot.getSingles().get(0);
        try {
            webtoonDto.setEpisodeThumbnailUrl(getThumbnailUrl(episode.getLandThumbnailUrl()));
            webtoonDto.setEpisodeNumber(episode.getOrderValue());
            webtoonDto.setEpisodeTitle(episode.getTitle());
            webtoonDto.setEpisodeRating("");
            webtoonDto.setEpisodeUrl(getViewerUrl(episode.getId()));

            String dateString = StringUtils.replace(
                    episode.getFreeChangeDt(), "-", "").substring(0, 8);
            webtoonDto.setEpisodeUpdateDate(dateString);
            webtoonDto.setLastUpdateDt(dateString);

            logger.trace("data: {}", webtoonDto);
        } catch (Exception e) {
            logger.error("error data: {}", webtoonDto);
            throw e;
        }
        return webtoonDto;
    }

    private String getViewerUrl(long id) {
        return String.format(KAKAO_CONSTANT.VIEWER_PATH, id);
    }

    private String getThumbnailUrl(String url) {
        return String.format(KAKAO_CONSTANT.THUMBNAIL_PATH, url);
    }
}