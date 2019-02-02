package com.manpu.crawler.cache;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.WebtoonWeek;
import com.manpu.crawler.service.WebtoonService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ManpuCacheMap {

    private Logger logger = LoggerFactory.getLogger(ManpuCacheMap.class);

    @Autowired
    private WebtoonService webtoonService;

    private ConcurrentHashMap<String, WebtoonCacheInfo> naverWebtoonCacheMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, WebtoonCacheInfo> daumWebtoonCacheMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, WebtoonCacheInfo> lezhinWebtoonCacheMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, WebtoonCacheInfo> kakaoWebtoonCacheMap = new ConcurrentHashMap<>();

    private String generateWebtoonId(String site, String crawledId) {
        return String.format("%s-%s",site, crawledId);
    }

    public int getMapCount(String site) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        return targetMap.size();
    }

    public void putWebtoon(String site, String crawledId, WebtoonCacheInfo webtoonCacheInfo) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        targetMap.put(generateWebtoonId(site, crawledId), webtoonCacheInfo);
    }

    public int getLastEpiNumber(String site, String crawledId) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        WebtoonCacheInfo webtoonCacheInfo = targetMap.get(generateWebtoonId(site, crawledId));
        if(webtoonCacheInfo == null) {
            return -1;
        }
        return webtoonCacheInfo.getLastEpisodeNum();
    }

    private Map<String, WebtoonCacheInfo> getCacheMap(String site) {
        SITE targetSite;
        Map<String, WebtoonCacheInfo> targetMap = null;

        try {
            targetSite = SITE.valueOf(site);
        } catch(Exception e) {
            throw new RuntimeException("invalid site name");
        }

        switch(targetSite) {
            case NAVER: {
                targetMap = this.naverWebtoonCacheMap;
                break;
            }
            case DAUM: {
                targetMap = this.daumWebtoonCacheMap;
                break;
            }
            case LEZHIN: {
                targetMap = this.lezhinWebtoonCacheMap;
                break;
            }
            case KAKAO: {
                targetMap = this.kakaoWebtoonCacheMap;
                break;
            }
            default: {
                break;
            }
        }
        return targetMap;
    }

    public boolean isExist(String site, String crawledId) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        return targetMap.containsKey(generateWebtoonId(site, crawledId));
    }

    public void collectWebtoonCache() {
        List<Webtoon> webtoonList = webtoonService.getWebtoonInSerial();
        if(webtoonList == null) {
            return;
        }
        for(Webtoon webtoon : webtoonList) {
            if(!this.isExist(webtoon.getSite(), webtoon.getCrawledId())) {
                List<WebtoonWeek> webtoonWeek = webtoonService.getWebtoonWeekInfo(webtoon);
                final HashMap<WEEK, Integer> weekMap = new HashMap<>();
                webtoonWeek.stream().forEach(w -> {
                    if(w.getRanking() != null) {
                        weekMap.put(w.getDay(), w.getRanking().intValue());
                    }
                });

                WebtoonCacheInfo cacheInfo = new WebtoonCacheInfo();
                cacheInfo.setLastEpisodeNum((int)webtoon.getLastEpisodeNum());
                cacheInfo.setWeekInfo(weekMap);
                putWebtoon(webtoon.getSite(), webtoon.getCrawledId(), cacheInfo);
            }
        }
    }

    public void removeWebtoonCache(List<WebtoonDto> webtoonDtoList) {
    	for(WebtoonDto webtoonDto : webtoonDtoList) {
    		removeWebtoonCache(webtoonDto.getSite(),webtoonDto.getCrawledId());
    	}
    }
    
    public void removeWebtoonCache(String site, String crawledId) {
    	Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
    	targetMap.remove(generateWebtoonId(site, crawledId));
    }
    
    public void updateWebtoonCache(String site, String crawledId, WebtoonDto webtoonDto) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        String webtoonId = generateWebtoonId(site, crawledId);
        WebtoonCacheInfo cacheInfo = targetMap.get(webtoonId);
        if(cacheInfo == null) {
            cacheInfo = new WebtoonCacheInfo();
        }
        cacheInfo.setLastEpisodeNum(webtoonDto.getEpisodeNumber());

        Map<WEEK, Integer> cacheWeekInfo = cacheInfo.getWeekInfo();
        if(cacheWeekInfo == null) {
            cacheWeekInfo = new HashMap();
        }
        cacheWeekInfo.putAll(webtoonDto.getWeekInfo());
        cacheInfo.setWeekInfo(cacheWeekInfo);
        targetMap.put(generateWebtoonId(site, crawledId), cacheInfo);
    }

    public Map<WEEK, Integer> getLastRankingMap(String site, String crawledId) {
        Map<String, WebtoonCacheInfo> targetMap = getCacheMap(site);
        WebtoonCacheInfo webtoonCacheInfo = targetMap.get(generateWebtoonId(site, crawledId));
        if(webtoonCacheInfo == null && webtoonCacheInfo.getWeekInfo() == null) {
            return null;
        }
        return webtoonCacheInfo.getWeekInfo();
    }

    @Data
    private class WebtoonCacheInfo {
        private int lastEpisodeNum;
        private Map<WEEK, Integer> weekInfo;
    }
}