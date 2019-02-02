package com.manpu.crawler.worker.crawler.kakao;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.KAKAO_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.kakao.model.KakaoListDataRoot;
import com.manpu.crawler.worker.crawler.kakao.model.KakaoWebtoon;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class KakaoListCrawler {

    private Logger logger = LoggerFactory.getLogger(KakaoListCrawler.class);

    public List<WebtoonDto> work() {
        final String url = KAKAO_CONSTANT.COMIC_HOST + KAKAO_CONSTANT.MAIN_URL;
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        KakaoListDataRoot kakaoListDataRoot = JacksonJsonHelper.convertToObject(KakaoListDataRoot.class, document.body().html());
        logger.trace(document.body().html());


        if(kakaoListDataRoot != null &&
                ! (kakaoListDataRoot.getResultCode() == 0)) {
            logger.error("list table data is null");
            return null;
        }

        List<KakaoWebtoon> webtoons = kakaoListDataRoot.getSectionContainers().get(0).getSectionSeries().get(0).getList();
        final AtomicInteger counter = new AtomicInteger(0);
        List<WebtoonDto> webtoonDtos = webtoons.stream().map(webtoonData -> {
            WebtoonDto webtoonDto = new WebtoonDto();

            Map<WEEK, Integer> weekInfo = new HashMap<>();
            weekInfo.put(WEEK.na, counter.incrementAndGet());
            webtoonDto.setWeekInfo(weekInfo);

            webtoonDto.setSite(SITE.KAKAO.name());
            webtoonDto.setSerialState(SerialState.ACTIVE);
            webtoonDto.setThumbnailUrl(String.format(KAKAO_CONSTANT.THUMBNAIL_PATH,webtoonData.getImage()));
            webtoonDto.setCrawledId(String.valueOf(webtoonData.getSeriesId()));
            webtoonDto.setUrl(String.format(KAKAO_CONSTANT.DETAIL_PATH, webtoonData.getSeriesId()));

            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z:\\s]";
            webtoonDto.setWebtoonTitle(StringUtils.replaceAll(webtoonData.getTitle(),
                    match, "").trim());
            webtoonDto.setLastUpdateDt(StringUtils.replace(webtoonData.getLastSlideAddedDate(), "-", "")
                    .substring(0,8));

            String[] artists = StringUtils.split(webtoonData.getAuthor(), ",");
            List<ArtistDto> artistDtoList = new ArrayList<>();
            for(String artist : artists) {
                ArtistDto artistDto = new ArtistDto();
                artistDto.setArtistId(StringUtils.trim(artist));
                artistDto.setNickName(StringUtils.trim(artist));
                artistDtoList.add(artistDto);
            }
            webtoonDto.setArtistDtoList(artistDtoList);

            return webtoonDto;

        }).collect(Collectors.toList());

        logger.trace("data: {}", webtoonDtos);
        return webtoonDtos;
    }
}
