package com.manpu.crawler.worker.crawler.lezhin.v2;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT.V2;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_WEEK;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.ArtistDto;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon.SerialState;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.lezhin.v2.model.*;
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
import java.util.stream.Collectors;

import static com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_WEEK.*;

@Component
public class LezhinListCrawlerV2 {

    private Logger logger = LoggerFactory.getLogger(LezhinListCrawlerV2.class);

    public List<WebtoonDto> work() {

        // 순서 정렬을 위한 ranking order 캐시 맵
        Map<WEEK, Integer> weekRankingCacheMap = new HashMap<>();
        for (WEEK week : WEEK.values()) {
            weekRankingCacheMap.put(week, 1);
        }

        // 실제 랭킹 정보를 수집하여 처리하는 것은 보류
        // Map<Long, Integer> rankingMap = getRankingMap();

        final String url = String.format(LEZHIN_CONSTANT.COMIC_HOST + LEZHIN_CONSTANT.V2.MAIN_URL, System.currentTimeMillis());
        Document document;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-LZ-Adult", "0");
            headers.put("X-LZ-AllowAdult", "false");
            headers.put("X-LZ-Country", "kr");
            headers.put("X-LZ-Locale", "ko-KR");
            document = JsoupHelper.connectWithHaeder(url, JSOUP_METHOD.GET, headers);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        LezhinRoot data = JacksonJsonHelper.convertToObject(LezhinRoot.class, document.body().html());

        List<LezhinInventory> inventoryList = data.getData().getInventoryList();

        Map<Long, WebtoonDto> webtoonDtoMap = new HashMap<>();
        for(final LEZHIN_WEEK week : values()) {
            if(week.equals(LEZHIN_WEEK.recommend) || week.equals(LEZHIN_WEEK.pass)) {
                continue;
            }
            List<LezhinItem> weekItems = inventoryList.get(week.getIndex()).getItems();
            for(int i = 0 ; i < weekItems.size() ; i++) {
                LezhinItem item = weekItems.get(i);
                WebtoonDto webtoonDto = processingWebtoonData(webtoonDtoMap, weekRankingCacheMap, week, item);
                webtoonDtoMap.put(item.getIdLezhinObject(), webtoonDto);
            }
        }

        // 첫번째 인벤토리는 요일 별 추천 만화
        List<LezhinItem> recommendItems = inventoryList.get(0).getItems();
        for(int i = 0 ; i < recommendItems.size() ; i++) {
            LezhinItem item = recommendItems.get(i);
            WebtoonDto webtoonDto = processingWebtoonData(webtoonDtoMap, weekRankingCacheMap, getWeek(i), item);
            webtoonDtoMap.put(item.getIdLezhinObject(), webtoonDto);
        }
        logger.trace("data: {}", webtoonDtoMap);
        return new ArrayList<>(webtoonDtoMap.values());
    }

    private WebtoonDto processingWebtoonData(Map<Long,WebtoonDto> webtoonDtoMap
            , Map<WEEK, Integer> weekRankingCacheMap
            , LEZHIN_WEEK week, LezhinItem item) {
        WebtoonDto webtoonDto;
        if(webtoonDtoMap.get(item.getIdLezhinObject()) != null) {
            // 주 2회 이상 연재 할 경우
            webtoonDto = webtoonDtoMap.get(item.getIdLezhinObject());
            WEEK webtoonWeek = WEEK.valueOf(week.name());
            int currentWeekRanking = weekRankingCacheMap.get(webtoonWeek);
            webtoonDto.getWeekInfo().put(webtoonWeek, currentWeekRanking);
            weekRankingCacheMap.put(webtoonWeek, currentWeekRanking + 1);
        } else {
            webtoonDto = new WebtoonDto();
            Map<WEEK, Integer> weekInfo = new HashMap<>();
            if(week == ten) {
                logger.warn("lezhin - ten day: {}", item.getTitle());
                int thuRanking = weekRankingCacheMap.get(WEEK.thu);
                int sunRanking = weekRankingCacheMap.get(WEEK.sun);
                weekInfo.put(WEEK.thu, thuRanking);
                weekRankingCacheMap.put(WEEK.thu, thuRanking + 1);
                weekInfo.put(WEEK.sun, sunRanking);
                weekRankingCacheMap.put(WEEK.sun, sunRanking + 1);
            } else {
                WEEK webtoonWeek = WEEK.valueOf(week.name());
                int currentWeekRanking = weekRankingCacheMap.get(webtoonWeek);
                weekInfo.put(webtoonWeek, currentWeekRanking);
                weekRankingCacheMap.put(webtoonWeek, currentWeekRanking + 1);
            }
            webtoonDto.setWeekInfo(weekInfo);
            webtoonDto.setSite(SITE.LEZHIN.name());
            webtoonDto.setCrawledId(item.getAlias());
            webtoonDto.setSerialState(SerialState.ACTIVE);
            webtoonDto.setWebtoonTitle(item.getTitle());
            webtoonDto.setArtistDtoList(getArtistDto(item.getAuthors()));
            webtoonDto.setUrl(LEZHIN_CONSTANT.COMIC_HOST + item.getTargetUrl());
            webtoonDto.setMainPageUrl(LEZHIN_CONSTANT.COMIC_HOST + item.getTargetUrl());
            webtoonDto.setThumbnailUrl(getThumbnailUrl(item.getMediaList()));
        }
        return webtoonDto;
    }

    private LEZHIN_WEEK getWeek(int index) {
        switch(index) {
            case 0:
                return mon;
            case 1:
                return tue;
            case 2:
                return wed;
            case 3:
                return thu;
            case 4:
                return fri;
            case 5:
                return sat;
            case 6:
                return sun;
            case 7:
                return sun;
        }
        return null;
    }

    private List<ArtistDto> getArtistDto(List<LezhinAuthor> artists) {
        return artists.stream().map(artist -> {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setNickName(StringUtils.trim(artist.getName()));
            artistDto.setArtistId(StringUtils.trim(artist.getId()));
            return artistDto;
        }).collect(Collectors.toList());
    }

    private Map<Long,Integer> getRankingMap() {
        final String rankingUrl = String.format(LEZHIN_CONSTANT.COMIC_HOST + V2.RANKING_URL, System.currentTimeMillis());
        Document rankingDocument;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-LZ-Adult", "0");
            headers.put("X-LZ-AllowAdult", "false");
            headers.put("X-LZ-Country", "kr");
            headers.put("X-LZ-Locale", "ko-KR");
            rankingDocument = JsoupHelper.connectWithHaeder(rankingUrl, JSOUP_METHOD.GET, headers);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        LezhinRankingDataRoot rankingData = JacksonJsonHelper.convertToObject(LezhinRankingDataRoot.class
                , rankingDocument.body().html());

        Map<Long, Integer> rankingMap = new HashMap<>();
        for(int i = 0 ; i < rankingData.getData().size() ; i++) {
            rankingMap.put(rankingData.getData().get(i).getId(), i);
        }
        return rankingMap;
    }

    private String getThumbnailUrl(List<LezhinMedia> mediaList) {
        for(LezhinMedia media: mediaList) {
            if(media.getMediaKey().indexOf("thumbnails") != -1) {
                return MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT.V2.THUMBNAIL_HOST + media.getUrl();
            }
        }
        return null;
    }
}
