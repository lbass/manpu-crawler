package com.manpu.crawler.worker.crawler.daum;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.DAUM_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.WebtoonUrlGenerator;
import com.manpu.crawler.worker.crawler.daum.model.DaumArtist;
import com.manpu.crawler.worker.crawler.daum.model.DaumResponse;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DaumListCrawler {

    private Logger logger = LoggerFactory.getLogger(DaumListCrawler.class);

    public List<WebtoonDto> work(WEEK week) {
        final String url = String.format(DAUM_CONSTANT.COMIC_HOST + DAUM_CONSTANT.MAIN_URL,
                week.name(), System.currentTimeMillis());
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        DaumResponse daumResponse = JacksonJsonHelper.convertToObject(DaumResponse.class, document.body().html());
        logger.trace(daumResponse.toString());

        if(daumResponse != null &&
                !daumResponse.getResult().getStatus().equalsIgnoreCase(
                        String.valueOf(HttpStatus.OK.value()))) {
            logger.error("list table data is null");
            return null;
        }

        final AtomicInteger counter = new AtomicInteger(0);
        List<WebtoonDto> webtoonDtos = daumResponse.getWebtoonList().stream().map(webtoonData -> {
            WebtoonDto webtoonDto = new WebtoonDto();
            webtoonDto.setSite(SITE.DAUM.name());

            Map<WEEK, Integer> weekInfo = new HashMap<>();
            weekInfo.put(week, counter.incrementAndGet());

            webtoonDto.setWeekInfo(weekInfo);
            if("Y".equalsIgnoreCase(webtoonData.getRestYn())) {
                webtoonDto.setSerialState(SerialState.DORMANT);
            } else {
                webtoonDto.setSerialState(SerialState.ACTIVE);
            }

            webtoonDto.setThumbnailUrl(webtoonData.getThumbnailImage().getImageUrl());
            webtoonDto.setCrawledId(webtoonData.getNickname());
            webtoonDto.setWebtoonTitle(webtoonData.getTitle());

            /*
            if(webtoonData.getLastEpisode() != null) {
                DaumEpisode episode = webtoonData.getLastEpisode();
                webtoonDto.setEpisodeUpdateDate(episode.getDateCreated().substring(0,8));
                webtoonDto.setEpisodeUrl(DAUM_CONSTANT.VIEWER_PATH + String.valueOf(episode.getEpisodeId()));
                webtoonDto.setEpisodeRating("");
                webtoonDto.setEpisodeNumber(episode.getEpisodeNumber());
                webtoonDto.setEpisodeThumbnailUrl(episode.getEpisodeThumbnailImage().getImageUrl());
                webtoonDto.setEpisodeTitle(episode.getTitle());
                webtoonDto.setLastUpdateDt(episode.getDateCreated().substring(0,8));
            }
            */

            if(webtoonData.getCartoon() != null) {
                List<DaumArtist> artists = webtoonData.getCartoon().getArtists();
                List<ArtistDto> artistsList = artists.stream().distinct().map(daumArtist -> {
                    ArtistDto artistDto = new ArtistDto();
                    artistDto.setArtistId(String.valueOf(daumArtist.getArtistId()));
                    artistDto.setNickName(StringUtils.trim(daumArtist.getName()));
                    return artistDto;
                }).collect(Collectors.toList());
                webtoonDto.setArtistDtoList(artistsList);
            }

            BigDecimal ratingScore = new BigDecimal(webtoonData.getAverageScore());
            webtoonDto.setRating(ratingScore.setScale(2, BigDecimal.ROUND_DOWN).toString());
            webtoonDto.setUrl(WebtoonUrlGenerator.getWebtoonDataUrl(SITE.DAUM, webtoonDto.getCrawledId()));
            webtoonDto.setMainPageUrl(WebtoonUrlGenerator.getWebtoonMainPageUrl(SITE.DAUM, webtoonDto.getCrawledId()));
            return webtoonDto;

        }).collect(Collectors.toList());

        logger.trace("data: {}", webtoonDtos);
        return webtoonDtos;
    }
}
