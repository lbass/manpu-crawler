package com.manpu.crawler.worker.crawler.lezhin.v1;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.dto.WebtoonDto;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.metric.CrawlerMetric;
import com.manpu.crawler.worker.crawler.WebtoonCrawler;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinDetailRoot;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinEpisode;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class LezhinWebtoonCrawler implements WebtoonCrawler {

    private Logger logger = LoggerFactory.getLogger(LezhinWebtoonCrawler.class);

    @Autowired
    private CrawlerMetric crawlerMetric;

    @Override
    public WebtoonDto process(WebtoonDto webtoonDto) {
        final String url = webtoonDto.getUrl();
        Document document = null;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            logger.error("Lezhin - detail timeout {}", url);
            return null;
        }

        crawlerMetric.addLezhinEpisodeWorkCount();
        Elements scripts = document.select("script");

        StringBuffer jsonData = new StringBuffer();
        boolean isLoopBraek = false;
        for(int i = 0 ; scripts != null && i < scripts.size() ; i++) {
            Element script = scripts.get(i);
            BufferedReader reader = new BufferedReader(new StringReader(script.html()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("__LZ_PRODUCT__ =") != -1) {
                        jsonData.append("{");
                        while((line = reader.readLine()) != null) {
                            line = StringUtils.trim(reader.readLine());
                            if(line.startsWith("product")) {
                                line = line.replaceFirst("product", "\"product\"");
                                jsonData.append(line);
                                jsonData = jsonData.delete(jsonData.length() - 1, jsonData.length());
                                break;
                            }
                        }
                        logger.trace("line: {}", jsonData.toString());
                        jsonData.append("}");
                        isLoopBraek = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(isLoopBraek) {
                break;
            }
        }

        try {
            LezhinDetailRoot data = JacksonJsonHelper.convertToObject(LezhinDetailRoot.class, jsonData.toString());
            long webtoonId = data.getProduct().getId();
            List<LezhinEpisode> episodes = data.getProduct().getEpisodes();
            int lastEpisodeNum = 1;
            int lastEpisodeIndex = 0;
            long todayMillisecond = DateHelper.getTodayMillisecond();
            List<Long> payEpisodes = new ArrayList<>();
            for(int i = 0 ; i < episodes.size() ; i++) {
                LezhinEpisode episode = episodes.get(i);
                if(episode.getSeq() > lastEpisodeNum) {

                    if(episode.getFreedAt() != null && episode.getFreedAt() != 0L) {
                        long millisecond = episode.getFreedAt();
                        if(!(millisecond > todayMillisecond)) {
                            lastEpisodeNum = episode.getSeq();
                            lastEpisodeIndex = i;
                        }
                    }
                }
                /*
                if(episode.getFreedAt() != null && episode.getFreedAt() != 0L) {
                    long millisecond = episode.getFreedAt();
                    if(millisecond > todayMillisecond) {
                        payEpisodes.add((long)episode.getSeq());
                    }
                } else {
                    payEpisodes.add((long)episode.getSeq());
                }
                */
            }

            logger.debug("{} {} : last episode {}", webtoonDto.getCrawledId(), webtoonDto.getWebtoonTitle(), lastEpisodeNum);
            LezhinEpisode episode = episodes.get(lastEpisodeIndex);
            webtoonDto.setEpisodeThumbnailUrl(getThumbnailUrl(webtoonId, episode.getId(), episode.getUpdatedAt()));
            webtoonDto.setEpisodeNumber(episode.getSeq());
            webtoonDto.setEpisodeTitle(episode.getDisplay().getTitle());
            webtoonDto.setEpisodeRating("");
            webtoonDto.setEpisodeUrl(url + "/" + episode.getName());
            webtoonDto.setPayEpisodeNumbers(payEpisodes);

            String dateString = DateHelper.getLongToDateString(episode.getPublishedAt(), "yyyyMMdd");
            webtoonDto.setEpisodeUpdateDate(dateString);
            webtoonDto.setLastUpdateDt(dateString);

            logger.trace("data: {}", webtoonDto);
        } catch (Exception e) {
            logger.error("error url: {}", url);
            logger.error("error data: {}", webtoonDto);
            throw e;
        }
        return webtoonDto;
    }

    private String getThumbnailUrl(long webtoonId, long id, long updatedAt) {
        return String.format(LEZHIN_CONSTANT.V1.EPISODE_THUMBNAIL_WIDTH, webtoonId, id, updatedAt);
    }
}
