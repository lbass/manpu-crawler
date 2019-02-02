package com.manpu.crawler.worker.crawler.lezhin.v1;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.WebtoonUrlGenerator;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinArtist;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinComic;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinDataRoot;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LezhinListCrawler {

    private Logger logger = LoggerFactory.getLogger(LezhinListCrawler.class);

    public List<WebtoonDto> work() {

        // 순서 정렬을 위한 ranking order 캐시 맵
        Map<WEEK, Integer> weekRankingCacheMap = new HashMap<>();
        for (WEEK week : WEEK.values()) {
            weekRankingCacheMap.put(week, 1);
        }

        final String url = String.format(LEZHIN_CONSTANT.COMIC_HOST + LEZHIN_CONSTANT.V2.MAIN_URL);
        Document document;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Element script = document.select("script").get(1);
        BufferedReader reader = new BufferedReader(new StringReader(script.html()));
        String line;
        StringBuffer jsonStb = new StringBuffer();
        try {
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("__LZ_DATA__ =") != -1) {
                    jsonStb.append("{");
                    line = StringUtils.trim(reader.readLine());
                    line = line.substring(0, line.length() - 1);
                    jsonStb.append(line);
                    jsonStb.append("}");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonData = jsonStb.toString();
        logger.trace("data: {}", jsonData);
        if(jsonData == null || jsonData.isEmpty()) {
            logger.trace("isAdult ? => {}", document.toString());
        }
        LezhinDataRoot data = JacksonJsonHelper.convertToObject(LezhinDataRoot.class, jsonData);

        List<WebtoonDto> webtoonDtos = new ArrayList<>();
        List<LezhinComic> comics = data.getComics();

        for (LezhinComic comic : comics) {
            SerialState serialState = getSerialState(comic.getAlias(), comic.getState());
            if (!SerialState.ACTIVE.equals(serialState)) {
                continue;
            }
            try {
                WebtoonDto webtoonDto = new WebtoonDto();
                Map<WEEK, Integer> weekInfo = new HashMap<>();
                List<String> days = comic.getProperties().getDays();
                if(days == null) {
                    // 런던 로망스 시리즈 case - 수집 제외해도 문제 없음
                    logger.warn("lezhin - days are null: {}", comic.getDisplay().getTitle());
                    continue;
                } else {
                    for (String day : days) {
                        WEEK week = getDayToWeek(day);
                        if(week == WEEK.na) {
                            // na는 5. 15. 25일 연재와 같이 월의 특정일에 업데이트되는 웹툰으로 목/일에 배치한다.
                            logger.warn("lezhin - na day: {}", comic.getDisplay().getTitle());
                            int thuRanking = weekRankingCacheMap.get(WEEK.thu);
                            int sunRanking = weekRankingCacheMap.get(WEEK.sun);
                            weekInfo.put(WEEK.thu, thuRanking);
                            weekRankingCacheMap.put(WEEK.thu, thuRanking + 1);
                            weekInfo.put(WEEK.sun, sunRanking);
                            weekRankingCacheMap.put(WEEK.sun, sunRanking + 1);
                        } else {
                            int currentWeekRanking = weekRankingCacheMap.get(week);
                            weekInfo.put(week, currentWeekRanking);
                            weekRankingCacheMap.put(week, currentWeekRanking + 1);
                        }
                    }
                }
                webtoonDto.setWeekInfo(weekInfo);
                webtoonDto.setSite(SITE.LEZHIN.name());
                webtoonDto.setCrawledId(comic.getAlias());
                webtoonDto.setSerialState(serialState);
                webtoonDto.setWebtoonTitle(comic.getDisplay().getTitle());
                webtoonDto.setArtistDtoList(getArtistDto(comic.getArtists()));
                webtoonDto.setUrl(WebtoonUrlGenerator.getWebtoonMainPageUrl(SITE.LEZHIN, comic.getAlias()));
                webtoonDto.setUrl(WebtoonUrlGenerator.getWebtoonDataUrl(SITE.LEZHIN, comic.getAlias()));
                webtoonDto.setThumbnailUrl(getThumbnailUrl(comic.getId(), comic.getUpdatedAt()));

                webtoonDtos.add(webtoonDto);

            } catch (Exception e) {
                logger.error("lezhin error: {}", e);
                logger.error("error data: {}", comic);
            }
        }
        logger.trace("data: {}", webtoonDtos);
        return webtoonDtos;
    }

    private WEEK getDayToWeek(String day) {
        if (day == null) {
            return WEEK.na;
        }

        switch (day) {
            case "0":
                return WEEK.sun;
            case "1":
                return WEEK.mon;
            case "2":
                return WEEK.tue;
            case "3":
                return WEEK.wed;
            case "4":
                return WEEK.thu;
            case "5":
                return WEEK.fri;
            case "6":
                return WEEK.sat;
            default:
                return WEEK.na;
        }
    }

    private String getThumbnailUrl(long id, long updatedAt) {
        return String.format(LEZHIN_CONSTANT.V1.THUMBNAIL_PATH, id, updatedAt);
    }

    private List<ArtistDto> getArtistDto(List<LezhinArtist> artists) {
        return artists.stream().map(artist -> {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setNickName(StringUtils.trim(artist.getName()));
            artistDto.setArtistId(StringUtils.trim(artist.getId()));
            return artistDto;
        }).collect(Collectors.toList());
    }

    private SerialState getSerialState(String alias, String state) {
        switch (state) {
            case "scheduled":
                return SerialState.ACTIVE;
            case "completed":
                return SerialState.INACTIVE;
            default:
                logger.error("serial state is inValid: {}, {}", alias, state);
                return SerialState.DORMANT;

        }
    }
}
