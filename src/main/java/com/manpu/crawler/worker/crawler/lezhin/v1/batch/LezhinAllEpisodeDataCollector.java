package com.manpu.crawler.worker.crawler.lezhin.v1.batch;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.LEZHIN_CONSTANT;
import com.manpu.crawler.dto.EpisodeDto;
import com.manpu.crawler.helper.DateHelper;
import com.manpu.crawler.helper.JacksonJsonHelper;
import com.manpu.crawler.helper.JsoupHelper;
import com.manpu.crawler.helper.JsoupHelper.JSOUP_METHOD;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinDetailRoot;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinEpisode;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LezhinAllEpisodeDataCollector {

    private Logger logger = LoggerFactory.getLogger(LezhinAllEpisodeDataCollector.class);

    public List<EpisodeDto> work(String crawledId) {
        final String url = String.format(LEZHIN_CONSTANT.COMIC_HOST + LEZHIN_CONSTANT.V1.DETAIL_PATH + crawledId);

        logger.info("collet url {}", url);
        Document document = null;
        try {
            document = JsoupHelper.connect(url, JSOUP_METHOD.GET);
        } catch (IOException e) {
            logger.error("Lezhin - detail timeout");
            return null;
        }

        Elements scripts = document.select("script");

        StringBuffer jsonData = new StringBuffer();
        boolean isLoopBraek = false;
        for (int i = 0; scripts != null && i < scripts.size(); i++) {
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
            if (isLoopBraek) {
                break;
            }
        }

        List<EpisodeDto> episodeDtoList = new ArrayList<>();

        LezhinDetailRoot data = JacksonJsonHelper.convertToObject(LezhinDetailRoot.class, jsonData.toString());
        long webtoonId = data.getProduct().getId();
        List<LezhinEpisode> episodes = data.getProduct().getEpisodes();
        long todayMillisecond = DateHelper.getTodayMillisecond();
        for (int i = 0; i < episodes.size(); i++) {
            LezhinEpisode episode = episodes.get(i);
            EpisodeDto episodeDto = new EpisodeDto();
            if (episode.getFreedAt() != null && episode.getFreedAt() != 0L) {
                long millisecond = episode.getFreedAt();
                if (millisecond > todayMillisecond) {
                    episodeDto.setFree(false);
                }
            } else {
                episodeDto.setFree(false);
            }
            episodeDto.setEpisodeThumbnailUrl(getThumbnailUrl(webtoonId, episode.getId(), episode.getUpdatedAt()));
            episodeDto.setEpisodeNumber(episode.getSeq());
            episodeDto.setEpisodeTitle(episode.getDisplay().getTitle());
            episodeDto.setRating("");
            episodeDto.setUrl(url + "/" + episode.getName());
            String dateString = DateHelper.getLongToDateString(episode.getPublishedAt(), "yyyyMMdd");
            episodeDto.setUpdateDate(dateString);

            episodeDtoList.add(episodeDto);
        }

        return episodeDtoList;
    }

    private String getThumbnailUrl(long webtoonId, long id, long updatedAt) {
        return String.format(LEZHIN_CONSTANT.V1.EPISODE_THUMBNAIL_WIDTH, webtoonId, id, updatedAt);
    }
}
